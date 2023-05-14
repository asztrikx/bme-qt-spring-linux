package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.security.UserToUserDetails
import hu.vecsesiot.backend.timetable.TimetableRepository
import hu.vecsesiot.backend.user.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

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
		val user = userRepository.findById(principal.id).get()
		val timetable = timetableRepository.findById(id).get()
		user.bus!!.timetable = timetable
		logger.info("The user ({}) took up the timetable ({}) for line ({})", user.id, timetable.id, timetable.line.id)
		busRepository.save(user.bus)
	}

	@Transactional
	fun finishTimetable(){
		val authentication = SecurityContextHolder.getContext().authentication
		val principal = authentication.principal as UserToUserDetails
		val user = userRepository.findById(principal.id).get()
		val timetable = user.bus!!.timetable
		user.bus.timetable = null
		logger.info("The user ({}) finished the timetable ({}) for line ({})", user.id, timetable?.id, timetable?.line?.id)
		busRepository.save(user.bus)
	}
}