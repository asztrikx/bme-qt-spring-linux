package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.user.User
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FaultTicketService {
    @Autowired
    private lateinit var repository: FaultTicketRepository
}
