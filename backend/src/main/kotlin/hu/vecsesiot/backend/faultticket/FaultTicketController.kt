package hu.vecsesiot.backend.faultticket


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@Suppress("IMPLICIT_CAST_TO_ANY")
@RestController
@RequestMapping("/faultTickets")
class FaultTicketController {
	@Autowired
	private lateinit var service: FaultTicketService
}