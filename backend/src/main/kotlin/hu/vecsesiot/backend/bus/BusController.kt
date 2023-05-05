package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.timetable.Timetable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buses")
class BusController {
	@Autowired
	private lateinit var busService: BusService

	// TODO vszeg ezt default kiadja
	@GetMapping("/{:id}/faulttickets")
	fun getAllFaultTicket(@PathVariable id: Long): ResponseEntity<List<FaultTicket>> {
		return ResponseEntity.ofNullable(
			busService.getAllFaultTickets(id)
		)
	}

	// TODO vszeg ezt default kiadja
	@GetMapping("/{:id}/timetable")
	fun getTimeTable(@PathVariable id: Long): ResponseEntity<Timetable> {
		return ResponseEntity.ofNullable(
			busService.getTimeTable(id)
		)
	}
}
