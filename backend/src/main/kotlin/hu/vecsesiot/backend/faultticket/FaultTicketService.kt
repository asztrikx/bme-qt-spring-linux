package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.security.UserToUserDetails
import hu.vecsesiot.backend.user.UserRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FaultTicketService {
	@Autowired
	private lateinit var repository: FaultTicketRepository

	@Autowired
	private lateinit var userRepository: UserRepository
	@Transactional
	fun createTicket(ticket : FaultTicket){
		val authentication = SecurityContextHolder.getContext().authentication
		val principal = authentication.principal as UserToUserDetails
		val user = userRepository.findById(principal.id).get()
		ticket.user = user
		ticket.bus = user.bus!!
		repository.save(ticket)
	}

	@Transactional
	fun refreshState(id : Long, state : FaultTicket.State){
		val ticket = repository.findById(id).get()
		ticket.state = state
		if (state == FaultTicket.State.Resolved) ticket.resolveDate = LocalDateTime.now()
		repository.save(ticket)
	}
}
