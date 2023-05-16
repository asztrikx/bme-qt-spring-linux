package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.security.UserToUserDetails
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.user.RegisterDto
import hu.vecsesiot.backend.user.User
import hu.vecsesiot.backend.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class FaultTicketServiceTest {

	@Autowired
	private lateinit var service: FaultTicketService

	@MockBean
	private lateinit var repository: FaultTicketRepository

	@MockBean
	private lateinit var userRepository: UserRepository

	private lateinit var securityContext: SecurityContext
	private lateinit var authentication: Authentication
	private lateinit var testUserObj : User

	private lateinit var ticket: FaultTicket

	@BeforeEach
	fun setup(){
		testUserObj = User(1,"testuser","testemail", "Test User", "password", listOf("User"))
		ticket = FaultTicket(1, LocalDateTime.now(), null, "Test ticket", GPSCoordinate(0.0, 0.0))

		securityContext = Mockito.mock(SecurityContext::class.java)
		authentication = Mockito.mock(Authentication::class.java)
		SecurityContextHolder.setContext(securityContext)
		Mockito.`when`(securityContext.authentication).thenReturn(authentication)
		Mockito.`when`(authentication.principal).thenReturn(UserToUserDetails(testUserObj))
	}

	@Test
	fun testCreateTicketWithNonExistUser(){
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.empty())
		assertThrows<IllegalStateException> {
			service.createTicket(ticket)
		}

		Mockito.verify(repository, times(0)).save(any())
	}

	@Test
	fun testCreateTicketWithExistUserWithoutBus(){
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		assertThrows<IllegalStateException> {
			service.createTicket(ticket)
		}

		Mockito.verify(repository, times(0)).save(any())
	}

	@Test
	fun testCreateTicketWithExistUserWithBus(){
		val testBus = Bus(1, "111AAA")
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		testUserObj.bus = testBus

		assertDoesNotThrow {
			service.createTicket(ticket)
		}

		Mockito.verify(repository, times(1)).save(ticket)
	}

	@Test
	fun testRefreshNonExistTicket(){
		Mockito.`when`(repository.findById(ticket.id!!)).thenReturn(Optional.empty())

		assertThrows<IllegalStateException> {
			service.refreshState(ticket.id!!, FaultTicket.State.InProgress)
		}

		Mockito.verify(repository, times(0)).save(any())
		assert(ticket.resolveDate == null)
		assert(ticket.state == FaultTicket.State.Created)
	}

	@Test
	fun testRefreshTicketToInProgress(){
		Mockito.`when`(repository.findById(ticket.id!!)).thenReturn(Optional.of(ticket))
		assertDoesNotThrow {
			service.refreshState(ticket.id!!, FaultTicket.State.InProgress)
		}
		Mockito.verify(repository, times(1)).save(ticket)
		assert(ticket.resolveDate == null)
		assert(ticket.state == FaultTicket.State.InProgress)
	}

	@Test
	fun testRefreshTicketToResolved(){
		Mockito.`when`(repository.findById(ticket.id!!)).thenReturn(Optional.of(ticket))
		assertDoesNotThrow {
			service.refreshState(ticket.id!!, FaultTicket.State.Resolved)
		}
		Mockito.verify(repository, times(1)).save(ticket)
		assert(ticket.resolveDate != null)
		assert(ticket.state == FaultTicket.State.Resolved)
	}
}