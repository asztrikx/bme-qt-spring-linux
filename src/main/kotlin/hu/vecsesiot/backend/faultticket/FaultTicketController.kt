package hu.vecsesiot.backend.faultticket


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/faultTickets")
class FaultTicketController {

    @Autowired
    private lateinit var service: FaultTicketService

    @PostMapping("")
    fun addFaultTicket(@RequestBody faultTicket : FaultTicket) = service.addFaultTicket(faultTicket)

    @GetMapping("/{:id}")
    fun getFaultTicketById(@PathVariable id: Long) = service.findById(id)

    @GetMapping("/?name={:name}")
    fun getFaultTicketByUser(@RequestBody user : User) = service.findFaultTicketsByUser(user)

    @PutMapping("")
    fun updateFaultTicket(@RequestBody faultTicket : FaultTicket) = service.updateFaultTicket(faultTicket)

    @DeleteMapping("/{:id}")
    fun deleteFaultTicketById(@PathVariable id: Long) = service.deleteFaultTicketById(id)
}