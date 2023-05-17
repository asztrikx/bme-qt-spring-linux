package hu.vecsesiot.backend.user

import hu.vecsesiot.backend.email.EmailService
import hu.vecsesiot.backend.line.Line
import hu.vecsesiot.backend.line.LineRepository
import hu.vecsesiot.backend.security.UserToUserDetails
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import java.util.*


@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

	@Autowired
	private lateinit var service: UserService

	@MockBean
	private lateinit var repository: UserRepository

	@MockBean
	private lateinit var lineRepository : LineRepository

	@MockBean
	private lateinit var emailService: EmailService

	private lateinit var securityContext: SecurityContext
	private lateinit var authentication: Authentication
	private lateinit var testUser: RegisterDto
	private lateinit var testUserObj : User

	@BeforeEach
	fun setup(){
		testUser = RegisterDto("testemail", "testuser", "password", "Test User")
		testUserObj = User(1,"testuser","testemail", "Test User", "password", mutableListOf("User"))

		securityContext = Mockito.mock(SecurityContext::class.java)
		authentication = Mockito.mock(Authentication::class.java)
		SecurityContextHolder.setContext(securityContext)
		Mockito.`when`(securityContext.authentication).thenReturn(authentication)
		Mockito.`when`(authentication.principal).thenReturn(UserToUserDetails(testUserObj))
	}

	@Test
	fun testRegisterNewUser(){
		Mockito.`when`(repository.findUserByUsername("testuser")).thenReturn(null)
		Mockito.`when`(repository.save(any())).thenReturn(any())

		assertDoesNotThrow{
			service.register(testUser)
		}

		Mockito.verify(emailService, times(1)).sendEmailTemplate(anyString(), any())
	}

	@Test
	fun testRegisterExistUserAndThrowException(){
		Mockito.`when`(repository.findUserByUsername("testuser")).thenReturn(testUserObj)

		assertThrows<IllegalStateException> {
			service.register(testUser)
		}

		Mockito.verify(repository, times(0)).save(any())
		Mockito.verify(emailService, times(0)).sendEmailTemplate(anyString(), any())


	}

	@Test
	fun testDetails(){
		Mockito.`when`(authentication.principal).thenReturn(UserToUserDetails(testUserObj))
		val dto = service.details()

		assert(dto.roles.isNotEmpty())
		assert(dto.roles.size == 1)
		assert(dto.roles.contains("User"))
		assert(dto.id == 1L)
	}

	@Test
	fun testSubscribeForValidLineWithExistUser(){
		val testLine = Line(1, "Test line")
		testUserObj.lineSubscriptions = mutableListOf()
		Mockito.`when`(repository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(lineRepository.findById(1)).thenReturn(Optional.of(testLine))

		assertDoesNotThrow {
			service.subscribeForLine(1L)
		}
		assert(testUserObj.lineSubscriptions.contains(testLine))
		Mockito.verify(repository, times(1)).save(any())
	}

	@Test
	fun testSubscribeForValidLineWithExistUserAgain(){
		val testLine = Line(1, "Test line")
		testUserObj.lineSubscriptions = mutableListOf(testLine)
		Mockito.`when`(repository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(lineRepository.findById(1)).thenReturn(Optional.of(testLine))

		assertThrows<IllegalStateException> {
			service.subscribeForLine(1L)
		}
		Mockito.verify(repository, times(0)).save(any())
	}

	@Test
	fun testSubscribeForInValidLineWithExistUser(){
		Mockito.`when`(repository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(lineRepository.findById(1)).thenReturn(Optional.empty())

		assertThrows<IllegalStateException> {
			service.subscribeForLine(1L)
		}
		Mockito.verify(repository, times(0)).save(any())
	}

	@Test
	fun testSubscribeForValidLineWithNonExistUser(){
		val testLine = Line(1, "Test line")
		Mockito.`when`(repository.findById(testUserObj.id!!)).thenReturn(Optional.empty())
		Mockito.`when`(lineRepository.findById(1)).thenReturn(Optional.of(testLine))

		assertThrows<IllegalStateException> {
			service.subscribeForLine(1L)
		}
		Mockito.verify(repository, times(0)).save(any())
	}

	@Test
	fun testUnsubscribeFromValidLineWithExistUser(){
		val testLine = Line(1, "Test line")
		testUserObj.lineSubscriptions = mutableListOf(testLine)
		Mockito.`when`(repository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(lineRepository.findById(1)).thenReturn(Optional.of(testLine))

		assertDoesNotThrow {
			service.unsubscribeFromLine(1L)
		}
		assert(!testUserObj.lineSubscriptions.contains(testLine))
		Mockito.verify(repository, times(1)).save(any())
	}

	@Test
	fun testUnsubscribeFromValidLineWithExistUserAgain(){
		val testLine = Line(1, "Test line")
		testUserObj.lineSubscriptions = mutableListOf()
		Mockito.`when`(repository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(lineRepository.findById(1)).thenReturn(Optional.of(testLine))

		assertThrows<IllegalStateException> {
			service.unsubscribeFromLine(1L)
		}
		Mockito.verify(repository, times(0)).save(any())
	}

	@Test
	fun testUnsubscribeFromInValidLineWithExistUser(){
		Mockito.`when`(repository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(lineRepository.findById(1)).thenReturn(Optional.empty())

		assertThrows<IllegalStateException> {
			service.unsubscribeFromLine(1L)
		}

		Mockito.verify(repository, times(0)).save(any())
	}

	@Test
	fun testUnsubscribeFromValidLineWithNonExistUser(){
		val testLine = Line(1, "Test line")
		Mockito.`when`(repository.findById(testUserObj.id!!)).thenReturn(Optional.empty())
		Mockito.`when`(lineRepository.findById(1)).thenReturn(Optional.of(testLine))

		assertThrows<IllegalStateException> {
			service.unsubscribeFromLine(1L)
		}
		Mockito.verify(repository, times(0)).save(any())
	}


}