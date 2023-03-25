package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.user.User
import jakarta.transaction.Transactional
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class FaultTicketService {
    @Autowired
    private lateinit var repository: FaultTicketRepository

    @Transactional
    fun addFaultTicket(faultTicket : FaultTicket) = repository.save(faultTicket)

    fun findFaultTicketsByUser(user : User) = repository.findAllByUser(user)

    fun findById(id : Long) = repository.findById(id)

    //@Transactional
    //fun updateFaultTicket(faultTicket : FaultTicket) = repository.save(findById(faultTicket.id!!).getOrNull() ?: throw Exception("404"))

    @Transactional
    fun deleteFaultTicketById(id : Long) = repository.deleteById(id)
}
