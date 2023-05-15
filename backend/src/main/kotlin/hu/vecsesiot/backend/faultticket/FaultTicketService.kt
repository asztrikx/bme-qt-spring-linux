package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.bus.BusService
import hu.vecsesiot.backend.security.UserToUserDetails
import hu.vecsesiot.backend.user.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
class FaultTicketService {
	@Autowired
	private lateinit var repository: FaultTicketRepository

	@Autowired
	private lateinit var userRepository: UserRepository

	private val logger = LoggerFactory.getLogger(FaultTicketService::class.java)
	@Transactional
	fun createTicket(ticket : FaultTicket){
		val authentication = SecurityContextHolder.getContext().authentication
		val principal = authentication.principal as UserToUserDetails
		val user = userRepository.findById(principal.id).getOrNull()

		check(user != null)
		check(user.bus != null)

		ticket.user = user
		ticket.bus = user.bus
		logger.info("One faultticket was created by user ({}) to bus ({})", user.id, user.bus.id)
		repository.save(ticket)
	}

	@Transactional
	fun refreshState(id : Long, state : FaultTicket.State){
		val ticket = repository.findById(id).getOrNull()
		check(ticket != null)

		val oldState = ticket.state
		ticket.state = state
		if (state == FaultTicket.State.Resolved) ticket.resolveDate = LocalDateTime.now()
		logger.info("One faultticket's ({}) state has changed from {} to {}", ticket.id, oldState, ticket.state)
		repository.save(ticket)
	}
}
