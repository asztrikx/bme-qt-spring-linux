package hu.vecsesiot.backend.line

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FaultTicketService {
    @Autowired
    private lateinit var repository: FaultTicketRepository

    @Transactional
    fun addFaultTicket(faultTicket : FaultTicket) = repository.save(faultTicket)

    fun findFaultTicketsByUser(user : User) = repository.findAllByUser(user)

    fun findById(id : Long) = repository.findById(id)

    @Transactional
    fun updateFaultTicket(faultTicket : FaultTicket)
    {
        var existing = repository.read(faultTicket.getId())
        BeanUtils.copyProperties(faultTicket, existing, getNullPropertyNames(faultTicket))
        repository.save(existing)
    }

    @Transactional
    fun deleteFaultTicketById(id : Long) = repository.deleteById(id)
}