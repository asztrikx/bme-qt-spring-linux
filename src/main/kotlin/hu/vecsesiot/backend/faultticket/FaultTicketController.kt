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

    @PostMapping("")
    fun addFaultTicket(@RequestBody faultTicket: FaultTicket) = service.addFaultTicket(faultTicket)

    @GetMapping("/{:id}")
    fun getFaultTicketById(@PathVariable id: Long) = service.findById(id)

    @GetMapping("/?user")
    fun getFaultTicketByUser(@RequestBody user: User) = service.findFaultTicketsByUser(user)

    @PutMapping("")
    fun updateFaultTicket(@RequestBody faultTicket: FaultTicket) =
        if (faultTicket.id == null) ResponseEntity.notFound() else service.addFaultTicket(faultTicket)

    @DeleteMapping("/{:id}")
    fun deleteFaultTicketById(@PathVariable id: Long) = service.deleteFaultTicketById(id)
}