package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.bus.BusController
import hu.vecsesiot.backend.bus.BusRepository
import hu.vecsesiot.backend.email.EmailService
import hu.vecsesiot.backend.email.FaultNotificationTemplate
import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.stop.Stop
import hu.vecsesiot.backend.stop.StopRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class LineService {
	@Autowired
	private lateinit var repository: LineRepository

	@Autowired
	private lateinit var busRepository: BusRepository

	@Autowired
	private lateinit var stopRepository: StopRepository

	@Autowired
	private lateinit var emailService: EmailService

	private val logger = LoggerFactory.getLogger(BusController::class.java)

	@Transactional
	fun getTimeUntilStop(line: Line, stop: Stop): Duration {
		require(line.stops.contains(stop))

		var routeTimeToStop = Duration.ZERO
		for (section in line.route) {
			if (section.start != stop) {
				routeTimeToStop += section.timespan
			} else {
				break
			}
		}
		logger.debug("Time until stop ({}) on line ({}): {}", stop.id, line.id, routeTimeToStop)
		return routeTimeToStop
	}

	@Transactional
	fun getNextOperationalBusBeforeStop(lineId: Long, stopId: Long, time: LocalDateTime): Bus? {
		val line = repository.findById(lineId).get()
		val stop = stopRepository.findById(stopId).get()

		val timeUntilStop = getTimeUntilStop(line, stop)
		return busRepository.findByLine(line)
			.filter { Duration.between(it.timetable!!.startDate, time) < timeUntilStop }
			.filter { it.faultTickets.isEmpty() }
			.sortedBy { Duration.between(it.timetable!!.startDate, time) }
			.lastOrNull()
	}

	@Transactional
	fun getAllBrokenBusBeforeStop(lineId: Long, stopId: Long, time: LocalDateTime): List<Bus> {
		val line = repository.findById(lineId).get()
		val stop = stopRepository.findById(stopId).get()

		val timeUntilStop = getTimeUntilStop(line, stop)
		return busRepository.findByLine(line)
			.filter { Duration.between(it.timetable!!.startDate, time) < timeUntilStop }
			.sortedBy { Duration.between(it.timetable!!.startDate, time) }
			.filter { it.faultTickets.any { it.state != FaultTicket.State.Resolved } }
	}

	fun notifyAllSubscribedUserOnBreakdown(lineId: Long) {
		val line = repository.findById(lineId).get()
		for (user in line.subscribedUsers)
			emailService.sendEmailTemplate(
				user.email, FaultNotificationTemplate(
					user.name,
					line.name,
				)
			)
	}
}