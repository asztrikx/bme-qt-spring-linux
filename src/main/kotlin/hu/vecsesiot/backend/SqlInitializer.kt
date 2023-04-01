package hu.vecsesiot.backend

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.bus.BusRepository
import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.faultticket.FaultTicketRepository
import hu.vecsesiot.backend.line.Line
import hu.vecsesiot.backend.line.LineRepository
import hu.vecsesiot.backend.section.Section
import hu.vecsesiot.backend.section.SectionRepository
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.stop.Stop
import hu.vecsesiot.backend.stop.StopRepository
import hu.vecsesiot.backend.timetable.Timetable
import hu.vecsesiot.backend.timetable.TimetableRepository
import hu.vecsesiot.backend.user.User
import hu.vecsesiot.backend.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime

@Component
class SqlInitializer : ApplicationRunner {
	@Autowired
	private lateinit var userRepository: UserRepository

	@Autowired
	private lateinit var stopRepository: StopRepository

	@Autowired
	private lateinit var sectionRepository: SectionRepository

	@Autowired
	private lateinit var lineRepository: LineRepository

	@Autowired
	private lateinit var timetableRepository: TimetableRepository

	@Autowired
	private lateinit var busRepository: BusRepository

	@Autowired
	private lateinit var faultTicketRepository: FaultTicketRepository

	override fun run(args: ApplicationArguments) {
		val stops = getStops()
		val sections = getSections(stops)
		val lines = getLines(sections)
		val timetables = getTimetables(lines)
		val users = getUsers()
		val buses = getBuses(users, timetables)
		val tickets = getFaultTickets(buses)



		stops.forEach { stopRepository.save(it) }
		sections.forEach { sectionRepository.save(it) }
		lines.forEach { lineRepository.save(it) }
		timetables.forEach { timetableRepository.save(it) }
		users.forEach { userRepository.save(it) }
		buses.forEach { busRepository.save(it) }
		tickets.forEach { faultTicketRepository.save(it) }
	}

	private fun getStops(): List<Stop> {
		return mutableListOf<Stop>().apply {
			add(
				Stop(
					coordinate = GPSCoordinate(47.419804, 19.247528),
					name = "Autóbuszállomás"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.39992883987876, 19.25913066972342),
					name = "Erzsébet tér"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.40366317296768, 19.2533053122232),
					name = "Sportpálya"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.40128681145784, 19.249980961251165),
					name = "Széchenyi"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.4173987343762, 19.241270529370745),
					name = "Logisztikai központ"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.41502039841608, 19.26243241570361),
					name = "Vörösmarty utca"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.407785632881705, 19.281104668262078),
					name = "Iskola utca"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.399162080014555, 19.299996594292413),
					name = "Kőolajvezeték Vállalat"
				)
			)
			add(
				Stop(
					coordinate = GPSCoordinate(47.416556419436255, 19.25811216892971),
					name = "OTP lakótelep"
				)
			)
		}
	}

	private fun getSections(stops: List<Stop>): List<Section> {
		return mutableListOf<Section>().apply {
			add(
				Section(timespan = Duration.ofSeconds(120)).apply {
					start = stops[0]
					stop = stops[1]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(100)).apply {
					start = stops[1]
					stop = stops[2]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(90)).apply {
					start = stops[1]
					stop = stops[3]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(65)).apply {
					start = stops[2]
					stop = stops[4]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(34)).apply {
					start = stops[4]
					stop = stops[5]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(123)).apply {
					start = stops[3]
					stop = stops[7]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(145)).apply {
					start = stops[7]
					stop = stops[8]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(170)).apply {
					start = stops[6]
					stop = stops[4]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(53)).apply {
					start = stops[4]
					stop = stops[2]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(102)).apply {
					start = stops[2]
					stop = stops[0]
				}
			)
			add(
				Section(timespan = Duration.ofSeconds(200)).apply {
					start = stops[8]
					stop = stops[6]
				}
			)
		}
	}

	private fun getLines(sections: List<Section>): List<Line> {
		return mutableListOf<Line>().apply {
			add(
				Line(name = "1 - Hosszú járat").apply {
					route = listOf(sections[0], sections[1], sections[3], sections[4])
					stops = listOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
				}
			)
			add(
				Line(name = "2 - Körjárat").apply {
					route = listOf(sections[0], sections[2], sections[5], sections[6])
					stops = listOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
				}
			)
			add(
				Line(name = "3 - Visszirányú járat").apply {
					route = listOf(sections[10], sections[7], sections[8], sections[9])
					stops = listOf(*(route.map { it.start }.toTypedArray()), route.last().stop)
				}
			)
		}
	}

	private fun getTimetables(lines: List<Line>): List<Timetable> {
		return mutableListOf<Timetable>().apply {
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 16, 42)).apply {
					line = lines[0]
				}
			)
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 15, 10)).apply {
					line = lines[1]
				}
			)
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 14, 42)).apply {
					line = lines[2]
				}
			)
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 15, 42)).apply {
					line = lines[0]
				}
			)
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 15, 33)).apply {
					line = lines[1]
				}
			)
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 15, 57)).apply {
					line = lines[1]
				}
			)
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 16, 5)).apply {
					line = lines[1]
				}
			)
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 15, 42)).apply {
					line = lines[2]
				}
			)
			add(
				Timetable(startDate = LocalDateTime.of(2023, 3, 30, 16, 42)).apply {
					line = lines[2]
				}
			)
		}
	}

	private fun getUsers(): List<User> {
		return mutableListOf<User>().apply {
			add(
				User(username = "Nagy István")
			)
			add(
				User(username = "Gipsz Jakab")
			)
			add(
				User(username = "Csetneki Péter")
			)
			add(
				User(username = "Nagy István")
			)
		}
	}

	private fun getBuses(users: List<User>, timetables: List<Timetable>): List<Bus> {
		return mutableListOf<Bus>().apply {
			add(
				Bus(serialnumber = "AAA111").apply {
					user = users[3]
					timetable = timetables[3]
				}
			)
			add(
				Bus(serialnumber = "BBB222").apply {
					user = users[2]
					timetable = timetables[7]
				}
			)
			add(
				Bus(serialnumber = "CCC223").apply {
					user = users[1]
				}
			)
		}
	}

	private fun getFaultTickets(buses: List<Bus>): List<FaultTicket> {
		return mutableListOf<FaultTicket>().apply {
			add(
				FaultTicket(
					startDate = LocalDateTime.of(2023, 3, 30, 15, 45),
					description = "The engine is fucked up.",
					coordinate = GPSCoordinate(47.416674, 19.254866),
					state = FaultTicket.State.Created
				).apply {
					bus = buses[0]
					user = bus.user!!

				}
			)
			add(
				FaultTicket(
					startDate = LocalDateTime.of(2023, 3, 30, 15, 45),
					description = "The engine is fucked up and first door is locked.",
					coordinate = GPSCoordinate(47.416674, 19.254866),
					state = FaultTicket.State.InProgress
				).apply {
					bus = buses[1]
					user = bus.user!!

				}
			)
			add(FaultTicket(
				startDate = LocalDateTime.of(2023, 3, 30, 12, 23),
				description = "The engine is broken.",
				coordinate = GPSCoordinate(47.409671, 19.270102),
				resolveDate = LocalDateTime.of(2023, 3, 30, 13, 48),
				state = FaultTicket.State.Resolved
			).apply {
				bus = buses[0]
				user = bus.user!!

			}
			)
			add(
				FaultTicket(
					startDate = LocalDateTime.of(2023, 3, 30, 12, 45),
					description = "The doors are locked.",
					coordinate = GPSCoordinate(47.41458957260982, 19.25814141217378),
					resolveDate = LocalDateTime.of(2023, 3, 30, 13, 48),
					state = FaultTicket.State.Resolved
				).apply {
					bus = buses[1]
					user = bus.user!!

				}
			)
		}
	}
}