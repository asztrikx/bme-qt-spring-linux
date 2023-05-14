package hu.vecsesiot.backend.faultticket



import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/faultTickets")
class FaultTicketController {
	@Autowired
	private lateinit var service: FaultTicketService


	@PostMapping("/create")
	fun createFaultTicket(@RequestBody ticket: FaultTicket) = service.createTicket(ticket)

	@PostMapping("/{id}/refresh/{state}")
	fun refreshState(@PathVariable id : Long, @PathVariable state : FaultTicket.State) = service.refreshState(id, state)
}