package hu.vecsesiot.backend.faultticket



import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/faultTickets")
class FaultTicketController {
	@Autowired
	private lateinit var service: FaultTicketService


	@PostMapping("/create")
	fun createFaultTicket(@RequestBody ticket: FaultTicket): ResponseEntity<Nothing> = try {
		service.createTicket(ticket)
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