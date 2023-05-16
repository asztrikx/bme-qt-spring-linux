package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.line.Line
import hu.vecsesiot.backend.security.UserToUserDetails
import hu.vecsesiot.backend.timetable.Timetable
import hu.vecsesiot.backend.timetable.TimetableRepository
import hu.vecsesiot.backend.user.User
import hu.vecsesiot.backend.user.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
class BusServiceTest {

	@Autowired
	private lateinit var service: BusService

	@MockBean
	private lateinit var busRepository: BusRepository

	@MockBean
	private lateinit var timetableRepository: TimetableRepository

	@MockBean
	private lateinit var userRepository: UserRepository

	private lateinit var securityContext: SecurityContext
	private lateinit var authentication: Authentication
	private lateinit var testUserObj : User
	private lateinit var timetable: Timetable


	@BeforeEach
	fun setup(){
		testUserObj = User(1,"testuser","testemail", "Test User", "password", listOf("User"))
		timetable = Timetable(1, LocalDateTime.now())
		timetable.line = Line(1, "Test line")

		securityContext = Mockito.mock(SecurityContext::class.java)
		authentication = Mockito.mock(Authentication::class.java)
		SecurityContextHolder.setContext(securityContext)
		Mockito.`when`(securityContext.authentication).thenReturn(authentication)
		Mockito.`when`(authentication.principal).thenReturn(UserToUserDetails(testUserObj))
	}

	@Test
	fun testTakeValidTimetableWithNonExistUser(){
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.empty())
		Mockito.`when`(timetableRepository.findById(timetable.id!!)).thenReturn(Optional.of(timetable))
		assertThrows<IllegalStateException> {
			service.takeTimetable(timetable.id!!)
		}
		Mockito.verify(busRepository, Mockito.times(0)).save(any())
	}

	@Test
	fun testTakeValidTimetableWithExistUserWithoutBus(){
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(timetableRepository.findById(timetable.id!!)).thenReturn(Optional.of(timetable))
		assertThrows<IllegalStateException> {
			service.takeTimetable(timetable.id!!)
		}
		Mockito.verify(busRepository, Mockito.times(0)).save(any())
	}

	@Test
	fun testTakeInvalidTimetableWithExistUserWithBus(){
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(timetableRepository.findById(timetable.id!!)).thenReturn(Optional.empty())
		testUserObj.bus = Bus(1, "111AAA")
		assertThrows<IllegalStateException> {
			service.takeTimetable(timetable.id!!)
		}
		Mockito.verify(busRepository, Mockito.times(0)).save(any())
	}

	@Test
	fun testTakeValidTimetableWithExistUserWithBusWithOutTimetable(){
		val testBus = Bus(1, "111AAA")
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(timetableRepository.findById(timetable.id!!)).thenReturn(Optional.of(timetable))
		testUserObj.bus = testBus
		assertDoesNotThrow {
			service.takeTimetable(timetable.id!!)
		}
		Mockito.verify(busRepository, Mockito.times(1)).save(testBus)
		assert(testBus.timetable == timetable)
	}

	@Test
	fun testTakeValidTimetableWithExistUserWithBusWithTimetable(){
		val testBus = Bus(1, "111AAA")
		testBus.timetable = Timetable(1, LocalDateTime.now())
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		Mockito.`when`(timetableRepository.findById(timetable.id!!)).thenReturn(Optional.of(timetable))
		testUserObj.bus = testBus
		assertThrows<IllegalStateException> {
			service.takeTimetable(timetable.id!!)
		}
		Mockito.verify(busRepository, Mockito.times(0)).save(any())
		assert(testBus.timetable != timetable)
	}

	@Test
	fun testFinishTimetableWithNonExistUser(){
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.empty())
		assertThrows<IllegalStateException> {
			service.finishTimetable()
		}
		Mockito.verify(busRepository, Mockito.times(0)).save(any())
	}

	@Test
	fun testFinishTimetableWithExistUserWithOutBus(){
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		assertThrows<IllegalStateException> {
			service.finishTimetable()
		}
		Mockito.verify(busRepository, Mockito.times(0)).save(any())
	}

	@Test
	fun testFinishTimetableWithExistUserWithBusWithoutTimetable(){
		val testBus = Bus(1, "111AAA")
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		testUserObj.bus = testBus
		assertThrows<IllegalStateException> {
			service.finishTimetable()
		}
		Mockito.verify(busRepository, Mockito.times(0)).save(any())
	}

	@Test
	fun testFinishTimetableWithExistUserWithBusWithTimetable(){
		val testBus = Bus(1, "111AAA")
		testBus.timetable = timetable
		Mockito.`when`(userRepository.findById(testUserObj.id!!)).thenReturn(Optional.of(testUserObj))
		testUserObj.bus = testBus

		assertDoesNotThrow {
			service.finishTimetable()
		}
		Mockito.verify(busRepository, Mockito.times(1)).save(testBus)
		assert(testBus.timetable == null)
	}

}