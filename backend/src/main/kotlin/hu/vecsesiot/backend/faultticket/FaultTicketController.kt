package hu.vecsesiot.backend.faultticket



import hu.vecsesiot.backend.line.LineService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/faultTickets")
class FaultTicketController {
	@Autowired
	private lateinit var service: FaultTicketService

	@Autowired
	private lateinit var lineService: LineService


	@PostMapping("/create")
	fun createFaultTicket(@RequestBody ticket: FaultTicket): ResponseEntity<Nothing> = try {
		val savedTicket = service.createTicket(ticket)
		lineService.notifyAllSubscribedUserOnBreakdown(savedTicket.bus.timetable!!.line.id!!)
		ResponseEntity.ok().build()
	} catch (e: Exception) {
		ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
	}

	@PostMapping("/{id}/refresh/{state}")
	fun refreshState(@PathVariable id : Long, @PathVariable state : FaultTicket.State) : ResponseEntity<Nothing> = try {
		service.refreshState(id, state)
		ResponseEntity.ok().build()
	} catch (e: Exception) {
		ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
	}
}