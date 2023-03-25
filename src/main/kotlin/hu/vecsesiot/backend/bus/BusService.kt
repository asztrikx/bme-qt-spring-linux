@file:OptIn(ExperimentalStdlibApi::class)

package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.timetable.Timetable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class BusService {
	@Autowired
	private lateinit var busRepository: BusRepository

	@Transactional
	fun getAllFaultTickets(id: Long): List<FaultTicket>? {
		return busRepository.findById(id).getOrNull()
			?.faultTickets
	}

	fun getTimeTable(id: Long): Timetable? {
		return busRepository.findById(id).getOrNull()
			?.timetable
	}
}