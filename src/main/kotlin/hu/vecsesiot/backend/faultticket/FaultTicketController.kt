package hu.vecsesiot.backend.faultticket


import hu.vecsesiot.backend.user.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Suppress("IMPLICIT_CAST_TO_ANY")
@RestController
@RequestMapping("/faultTickets")
class FaultTicketController {

    @Autowired
    private lateinit var service: FaultTicketService
}