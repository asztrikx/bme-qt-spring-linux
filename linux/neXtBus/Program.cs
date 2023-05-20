using System.Net;   
using System.Net.Http.Headers;
using System.Text;
using System.Text.Json;

namespace BusDisplay;
internal class Program
{
    private static List<KeyValuePair<int, string>> buses = new();
    private static int stopId;

    private static async Task SettingUp(HttpClient client)
    {
        stopId = GetStopID(client);
        string stopName;

        // getting stopName
        while ((stopName = (await GetStopName(client))!) is null) ;

        Console.WriteLine($"\nStarting display service in \"{stopName}\" stop!");

        while (true)
        {
            try
            {
                if (await getAllBusName(client) == 0)
                    break;
            }
            catch { }
            Thread.Sleep(5000);
        }
    }

    private static int GetStopID(HttpClient client)
    {
        // getting the stopID
        int stopId = -1;
        do
        {
            Console.Write("Please set stop id: ");
            try
            {
                stopId = Convert.ToInt32(Console.ReadLine());
                var resp = client.GetAsync($"stops/{stopId}");
                resp.Wait();
                if(resp.Result.StatusCode == HttpStatusCode.OK)
                    return stopId;
            } catch {}
            Console.WriteLine("Incorrect stop id or server is not accessible!");
        } while (true);
    }

    private static async Task<string?> GetStopName(HttpClient client)
    {
        try
        {
            // parsing json response
            var json = JsonDocument.Parse(await client.GetStringAsync($"stops/{stopId}"));
            if (json is null)
                return null;

            return json.RootElement.GetProperty("name").GetString();
        }
        catch { return null; }
    }

    private static async Task<int> getAllBusName(HttpClient client)
    {
        try
        {
            // parsing json response
            var json = JsonDocument.Parse(await client.GetStringAsync($"stops/{stopId}/lines"));
            var lineObjects = JsonSerializer.Deserialize<List<JsonElement>>(json.RootElement.GetProperty("_embedded").GetProperty("lines"));
            foreach (var item in lineObjects!)
            {
                if (buses.Where(x => x.Value == item.GetProperty("name").GetString()).Count() > 0)
                    continue;
                buses.Add(new KeyValuePair<int, string>
                (
                    Convert.ToInt32(item.GetProperty("_links").GetProperty("line").GetProperty("href").GetString()!.Split('/').LastOrDefault()),
                    item.GetProperty("name").GetString()!
                ));
                Console.WriteLine($"Line \"{buses.Last().Value}\" added to stop!");
            }
        }
        catch
        {
            throw new JsonException("Json parsing exception!");
        }
        return 0;
    }

    private static async Task<(int, DateTime)> getNextBusIndex(HttpClient client)
    {
        DateTime min = DateTime.MaxValue;
        int mini = -1;

        // finding the first avaible bus
        for (int i = 0; i < buses.Count; ++i)
        {
            try
            {
                var nextBus = Convert.ToDateTime(JsonDocument.Parse(await client.GetStringAsync($"lines/{buses[i].Key}/nextbustime/{stopId}")).RootElement.GetString());
                if (nextBus < min)
                {
                    min = nextBus;
                    mini = i;
                }
            }
            catch(HttpRequestException)
            {
                continue;
            }            
        }
        return (mini, min);
    }

    private static void writeBusIndex(int index, DateTime nextDate)
    {
        // opening filestream
        using StreamWriter w = new StreamWriter("/proc/busservice");
        if (w is null)
            throw new IOException("Coulden't open file \"/proc/busservice\"!");

        // checking range
        if (index > 10)
            throw new ArgumentException($"Value of pin index ({index}) is illegal!");

        if(index == -1)
        {
            w.Write("0:0");
            Console.WriteLine("No busses avaible on any line! Turning all indicator off!");
        }
        else
        {
            w.Write($"{index}:1");
            Console.WriteLine($"The next bus will arrive on line {buses[index].Key} at {nextDate}! Turning pin index {index} on.");
        }
    }

    static void Main(string[] args)
    {
        // setting the Http Client
        using HttpClient client = new();
        client.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Basic", Convert.ToBase64String(ASCIIEncoding.ASCII.GetBytes("disp:123")));
        
        String? addr = System.Environment.GetEnvironmentVariable("BASEADDRESS");
        if(addr is null)
        {
            Console.WriteLine("Base address not set in \"BASEADDRESS\" environment varialble!");
            return;
        }
        client.BaseAddress = new Uri($"http://{addr}:8080/api/");

        Console.WriteLine("Setting up VecsesIot neXtBus service\n");
        SettingUp(client).Wait();
        Console.WriteLine("Setup completed!\n\n");

        DateTime lastUpdated = DateTime.Now;

        // main loop
        while (true)
        {
            // setting next bus's display ID
            try
            {
                Task<(int, DateTime)> nextbusi = getNextBusIndex(client);
                nextbusi.Wait();
                writeBusIndex(nextbusi.Result.Item1, nextbusi.Result.Item2);
            }
            catch (Exception e)
            {
                if(e is IOException || e is ArgumentException)
                    Console.WriteLine(e.ToString().Split('\n')[0]);
            }

            // If an hour passed updtae 
            if ((DateTime.Now - lastUpdated).TotalMinutes > 60)
            {
                Console.WriteLine("Updating bus line list in stop.");
                getAllBusName(client).Wait();
                lastUpdated = DateTime.Now;
            }

            Thread.Sleep(5000);
        }
    }
}