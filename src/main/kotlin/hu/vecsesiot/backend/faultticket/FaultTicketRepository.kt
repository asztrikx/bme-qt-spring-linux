package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FaultTicketRepository : JpaRepository<FaultTicket, Long> {
    fun findAllByUser(user : User) : List<FaultTicket>
}