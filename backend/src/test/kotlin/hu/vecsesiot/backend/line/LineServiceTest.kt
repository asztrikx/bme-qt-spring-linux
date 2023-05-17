package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.bus.BusRepository
import hu.vecsesiot.backend.email.EmailService
import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.section.Section
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.stop.Stop
import hu.vecsesiot.backend.stop.StopRepository
import hu.vecsesiot.backend.timetable.Timetable
import hu.vecsesiot.backend.user.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.lang.IllegalArgumentException
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class LineServiceTest {
	@Autowired
	private lateinit var service: LineService

	@MockBean
	private lateinit var repository: LineRepository

	@MockBean
	private lateinit var busRepository: BusRepository

	@MockBean
	private lateinit var stopRepository: StopRepository

	@MockBean
	private lateinit var emailService: EmailService

	private lateinit var testLine : Line
	private lateinit var testStop1: Stop
	private lateinit var testStop2: Stop
	private lateinit var testUser1: User
	private lateinit var testUser2: User
	private lateinit var testBus1: Bus
	private lateinit var testBus2 : Bus

	@BeforeEach
	fun setup(){
		testLine = Line(1, "Test line")
		testUser1 = User(1, "testuser1", "testemail1", "Test User 1", "password", mutableListOf("User"))
		testUser2 = User(1, "testuser2", "testemail2", "Test User 2", "password", mutableListOf("User"))
		testStop1 = Stop(1, GPSCoordinate(0.0, 0.0), "Stop 1")
		testStop2 = Stop(2, GPSCoordinate(0.0, 0.0), "Stop 2")
		testBus1 = Bus(1, "111AAA")
		testBus2 = Bus(2, "222BBB")
	}

	@Test
	fun testSendNotificationWithoutSubscribers(){
		testLine.subscribedUsers = mutableListOf()
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		service.notifyAllSubscribedUserOnBreakdown(testLine.id!!)
		Mockito.verify(emailService, times(0)).sendEmailTemplate(anyString(), any())
	}

	@Test
	fun testSendNotificationWithOneSubscriber(){
		testLine.subscribedUsers = mutableListOf(testUser1)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		service.notifyAllSubscribedUserOnBreakdown(testLine.id!!)
		Mockito.verify(emailService, times(1)).sendEmailTemplate(anyString(), any())
	}

	@Test
	fun testSendNotificationWithMoreSubscribers(){
		testLine.subscribedUsers = mutableListOf(testUser1, testUser2)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		service.notifyAllSubscribedUserOnBreakdown(testLine.id!!)
		Mockito.verify(emailService, times(2)).sendEmailTemplate(anyString(), any())
	}

	@Test
	fun testGetTimeUntilStopWithoutStop(){
		testLine.stops = mutableListOf()
		assertThrows<IllegalArgumentException> {
			service.getTimeUntilStop(testLine, testStop1)
		}
	}

	@Test
	fun testGetTimeUntilStopWithStopIsTheFirst(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop1
			stop = testStop2
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		assert(service.getTimeUntilStop(testLine, testStop1) == Duration.ZERO)
	}

	@Test
	fun testGetTimeUntilStopWithStopIsTheSecond(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		assert(service.getTimeUntilStop(testLine, testStop1) == Duration.ofSeconds(200))
	}

	@Test
	fun testGetTimeUntilStopWithStopIsTheThird(){
		val testStop3 = Stop(3, GPSCoordinate(0.0, 0.0), "Stop 3")
		val section1 = Section(1, Duration.ofSeconds(100)).apply {
			start = testStop2
			stop = testStop1
		}
		val section2 = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop1
			stop = testStop3
		}
		testLine.stops = mutableListOf(testStop1, testStop2, testStop3)
		testLine.route = mutableListOf(section1, section2)
		assert(service.getTimeUntilStop(testLine, testStop1) == Duration.ofSeconds(100))
	}

	@Test
	fun testGetTimeUntilStopWithStopIsTheForth(){
		val testStop3 = Stop(3, GPSCoordinate(0.0, 0.0), "Stop 3")
		val section1 = Section(1, Duration.ofSeconds(100)).apply {
			start = testStop2
			stop = testStop3
		}
		val section2 = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop3
			stop = testStop1
		}
		testLine.stops = mutableListOf(testStop1, testStop2, testStop3)
		testLine.route = mutableListOf(section1, section2)
		assert(service.getTimeUntilStop(testLine, testStop1) == Duration.ofSeconds(300))
	}

	@Test
	fun testGetNextOperationalBusBeforeStopWithEmptyList(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop1
			stop = testStop2
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf())

		assert(service.getNextOperationalBusBeforeStop(testLine.id!!, testStop1.id!!, LocalDateTime.now()) == null)

	}

	@Test
	fun testGetNextOperationalBusBeforeStopWithBusTheTimeConditionDoesMatch(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		testBus1.timetable = Timetable(1, time.minusSeconds(201))
		testBus1.faultTickets = mutableListOf()
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1))

		assert(service.getNextOperationalBusBeforeStop(testLine.id!!, testStop1.id!!, time) == null)

	}

	@Test
	fun testGetNextOperationalBusBeforeStopWithBusesTheTimeConditionDoesMatch(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		testBus1.timetable = Timetable(1, time.minusSeconds(201))
		testBus1.faultTickets = mutableListOf()
		testBus2.timetable = Timetable(2, time.plusSeconds(1))
		testBus2.faultTickets = mutableListOf()
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1, testBus2))

		assert(service.getNextOperationalBusBeforeStop(testLine.id!!, testStop1.id!!, time) == testBus2)

	}

	@Test
	fun testGetNextOperationalBusBeforeStopWithBusTheTicketConditionDoesMatch(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		testBus1.timetable = Timetable(1, time.plusSeconds(1))
		testBus1.faultTickets = mutableListOf(FaultTicket(1, time.minusSeconds(5), description = "Ticket", coordinate = GPSCoordinate(0.0, 0.0)))
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1))

		assert(service.getNextOperationalBusBeforeStop(testLine.id!!, testStop1.id!!, time) == null)

	}

	@Test
	fun testGetNextOperationalBusBeforeStopWithBusesTheTicketConditionDoesMatch(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		testBus1.timetable = Timetable(1, time.plusSeconds(1))
		testBus1.faultTickets = mutableListOf(FaultTicket(1, time.minusSeconds(5), description = "Ticket", coordinate = GPSCoordinate(0.0, 0.0)))
		testBus2.timetable = Timetable(2, time.plusSeconds(1))
		testBus2.faultTickets = mutableListOf()
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1, testBus2))

		assert(service.getNextOperationalBusBeforeStop(testLine.id!!, testStop1.id!!, time) == testBus2)

	}

	@Test
	fun testGetNextOperationalBusBeforeStopWithBusesMatchAllCondition(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		testBus1.timetable = Timetable(1, time.plusSeconds(10))
		testBus1.faultTickets = mutableListOf()
		testBus2.timetable = Timetable(2, time.plusSeconds(100))
		testBus2.faultTickets = mutableListOf()
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1, testBus2))

		assert(service.getNextOperationalBusBeforeStop(testLine.id!!, testStop1.id!!, time) == testBus1)

	}

	@Test
	fun testGetNextOperationalBusTimeBeforeStopWithEmptyList(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop1
			stop = testStop2
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf())

		assert(service.getNextOperationalBusTimeBeforeStop(testLine.id!!, testStop1.id!!, LocalDateTime.now()) == null)

	}

	@Test
	fun testGetNextOperationalBusTimeBeforeStopWithBusTheTimeConditionDoesMatch(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		testBus1.timetable = Timetable(1, time.minusSeconds(201))
		testBus1.faultTickets = mutableListOf()
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1))

		assert(service.getNextOperationalBusTimeBeforeStop(testLine.id!!, testStop1.id!!, time) == null)

	}

	@Test
	fun testGetNextOperationalBusTimeBeforeStopWithBusesTheTimeConditionDoesMatch(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		val timetable = Timetable(2, time.plusSeconds(1))
		testBus1.timetable = Timetable(1, time.minusSeconds(201))
		testBus1.faultTickets = mutableListOf()
		testBus2.timetable = timetable
		testBus2.faultTickets = mutableListOf()
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1, testBus2))

		assert(service.getNextOperationalBusTimeBeforeStop(testLine.id!!, testStop1.id!!, time) == timetable.startDate.plusSeconds(200))

	}

	@Test
	fun testGetNextOperationalBusTimeBeforeStopWithBusTheTicketConditionDoesMatch(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		testBus1.timetable = Timetable(1, time.plusSeconds(1))
		testBus1.faultTickets = mutableListOf(FaultTicket(1, time.minusSeconds(5), description = "Ticket", coordinate = GPSCoordinate(0.0, 0.0)))
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1))

		assert(service.getNextOperationalBusTimeBeforeStop(testLine.id!!, testStop1.id!!, time) == null)

	}

	@Test
	fun testGetNextOperationalBusTimeBeforeStopWithBusesTheTicketConditionDoesMatch(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		val timetable = Timetable(2, time.plusSeconds(1))
		testBus1.timetable = Timetable(1, time.plusSeconds(1))
		testBus1.faultTickets = mutableListOf(FaultTicket(1, time.minusSeconds(5), description = "Ticket", coordinate = GPSCoordinate(0.0, 0.0)))
		testBus2.timetable = timetable
		testBus2.faultTickets = mutableListOf()
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1, testBus2))

		assert(service.getNextOperationalBusTimeBeforeStop(testLine.id!!, testStop1.id!!, time) == timetable.startDate.plusSeconds(200))

	}

	@Test
	fun testGetNextOperationalBusTimeBeforeStopWithBusesMatchAllCondition(){
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val time = LocalDateTime.now()
		val timetable = Timetable(1, time.plusSeconds(10))
		testBus1.timetable = timetable
		testBus1.faultTickets = mutableListOf()
		testBus2.timetable = Timetable(2, time.plusSeconds(100))
		testBus2.faultTickets = mutableListOf()
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1, testBus2))

		assert(service.getNextOperationalBusTimeBeforeStop(testLine.id!!, testStop1.id!!, time) == timetable.startDate.plusSeconds(200))

	}

	@Test
	fun testGetAllBrokenBusBeforeStopWithEmptyList(){
		val time = LocalDateTime.now()
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf())

		assert(service.getAllBrokenBusBeforeStop(testLine.id!!, testStop1.id!!, time).isEmpty())

	}

	@Test
	fun testGetAllBrokenBusBeforeStopWithBusTheTimeConditionDoesMatch(){
		val time = LocalDateTime.now()
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		val timetable = Timetable(1, time.minusSeconds(10))
		testBus1.timetable = timetable
		testBus1.faultTickets = mutableListOf()
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1))

		assert(service.getAllBrokenBusBeforeStop(testLine.id!!, testStop1.id!!, time).isEmpty())

	}

	@Test
	fun testGetAllBrokenBusBeforeStopWithBusesTheTimeConditionDoesMatch(){
		val time = LocalDateTime.now()
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		val timetable = Timetable(1, time.minusSeconds(10))
		testBus1.timetable = timetable
		testBus1.faultTickets = mutableListOf()
		testBus2.timetable = Timetable(2, time.minusSeconds(1))
		testBus2.faultTickets = mutableListOf()
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1, testBus2))

		assert(service.getAllBrokenBusBeforeStop(testLine.id!!, testStop1.id!!, time).isEmpty())

	}

	@Test
	fun testGetAllBrokenBusBeforeStopWithBusTheTicketConditionDoesMatch(){
		val time = LocalDateTime.now()
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		val timetable = Timetable(1, time.plusSeconds(10))
		testBus1.timetable = timetable
		testBus1.faultTickets = mutableListOf(FaultTicket(1, time.minusSeconds(5), description = "Ticket", coordinate = GPSCoordinate(0.0, 0.0), state = FaultTicket.State.Resolved))
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1))

		assert(service.getAllBrokenBusBeforeStop(testLine.id!!, testStop1.id!!, time).isEmpty())

	}

	@Test
	fun testGetAllBrokenBusBeforeStopWithBusMatchAllCondition(){
		val time = LocalDateTime.now()
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		val timetable = Timetable(1, time.plusSeconds(10))
		testBus1.timetable = timetable
		testBus1.faultTickets = mutableListOf(FaultTicket(1, time.minusSeconds(5), description = "Ticket", coordinate = GPSCoordinate(0.0, 0.0)))
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1))

		assert(service.getAllBrokenBusBeforeStop(testLine.id!!, testStop1.id!!, time).isNotEmpty())
		assert(service.getAllBrokenBusBeforeStop(testLine.id!!, testStop1.id!!, time).contains(testBus1))
	}

	@Test
	fun testGetAllBrokenBusBeforeStopWithBusMatchAllConditionWithMoreTicket(){
		val time = LocalDateTime.now()
		val section = Section(1, Duration.ofSeconds(200)).apply {
			start = testStop2
			stop = testStop1
		}
		val ticket1 = FaultTicket(1, time.minusSeconds(5), description = "Ticket", coordinate = GPSCoordinate(0.0, 0.0))
		val ticket2 = FaultTicket(2, time.minusSeconds(5), description = "Ticket", coordinate = GPSCoordinate(0.0, 0.0), state = FaultTicket.State.Resolved)
		testLine.stops = mutableListOf(testStop1, testStop2)
		testLine.route = mutableListOf(section)
		val timetable = Timetable(1, time.plusSeconds(10))
		testBus1.timetable = timetable
		testBus1.faultTickets = mutableListOf(ticket1, ticket2)
		Mockito.`when`(repository.findById(testLine.id!!)).thenReturn(Optional.of(testLine))
		Mockito.`when`(stopRepository.findById(testStop1.id!!)).thenReturn(Optional.of(testStop1))
		Mockito.`when`(busRepository.findByLine(testLine)).thenReturn(listOf(testBus1))

		assert(service.getAllBrokenBusBeforeStop(testLine.id!!, testStop1.id!!, time).isNotEmpty())
		assert(service.getAllBrokenBusBeforeStop(testLine.id!!, testStop1.id!!, time).contains(testBus1))
	}

}