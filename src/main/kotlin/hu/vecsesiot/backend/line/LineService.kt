package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.bus.BusRepository
import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.stop.Stop
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.Duration

@Service
class LineService {
	@Autowired
	private lateinit var repository: LineRepository

	@Autowired
	private lateinit var busRepository: BusRepository

	// TODO @Transactional
	fun getTimeUntilStop(line: Line, stop: Stop): Duration {
		// TODO validate whether stop is in the line

		var routeTimeToStop = Duration.ZERO
		// TODO can this fetch from db
		for (section in line.route) {
			if (section.start != stop) {
				routeTimeToStop += section.timespan;
			}
		}
		return routeTimeToStop
	}

	@Transactional
	fun getNextOperationalBusBeforeStop(line: Line, stop: Stop, time: LocalDateTime): Bus? {
		val timeUntilStop = getTimeUntilStop(line, stop)
		return busRepository.findByLine(line)
			.filter { Duration.between(it.timetable!!.startDate, time) < timeUntilStop }
			.filter { it.faultTickets.isEmpty() }
			.sortedBy { Duration.between(it.timetable!!.startDate, time) }
			.lastOrNull()
	}

	@Transactional
	fun getAllBrokenBusBeforeStop(line: Line, stop: Stop, time: LocalDateTime): List<Bus> {
		val timeUntilStop = getTimeUntilStop(line, stop)
		return busRepository.findByLine(line)
			.filter { Duration.between(it.timetable!!.startDate, time) < timeUntilStop }
			.sortedBy { Duration.between(it.timetable!!.startDate, time) }
			.filter { it.faultTickets.any { it.state != FaultTicket.State.Resolved } }
	}
}