package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.security.UserToUserDetails
import hu.vecsesiot.backend.timetable.TimetableRepository
import hu.vecsesiot.backend.user.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class BusService {
	@Autowired
	private lateinit var busRepository: BusRepository

	@Autowired
	private lateinit var timetableRepository: TimetableRepository

	@Autowired
	private lateinit var userRepository: UserRepository

	private val logger = LoggerFactory.getLogger(BusService::class.java)

	@Transactional
	fun takeTimetable(id: Long){
		val authentication = SecurityContextHolder.getContext().authentication
		val principal = authentication.principal as UserToUserDetails
		val user = userRepository.findById(principal.id).getOrNull()
		val timetable = timetableRepository.findById(id).getOrNull()

		check(user != null)

		val bus = user.bus

		check(bus != null)
		check(timetable != null)
		check(bus.timetable == null)

		bus.timetable = timetable
		logger.info("The user ({}) took up the timetable ({}) for line ({})", user.id, timetable.id, timetable.line.id)
		busRepository.save(bus)
	}

	@Transactional
	fun finishTimetable(){
		val authentication = SecurityContextHolder.getContext().authentication
		val principal = authentication.principal as UserToUserDetails
		val user = userRepository.findById(principal.id).getOrNull()

		check(user != null)

		val bus = user.bus

		check(bus != null)
		check(bus.timetable != null)

		val timetable = bus.timetable
		bus.timetable = null
		logger.info("The user ({}) finished the timetable ({}) for line ({})", user.id, timetable?.id, timetable?.line?.id)
		busRepository.save(bus)
	}
}