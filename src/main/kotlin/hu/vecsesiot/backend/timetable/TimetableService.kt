package hu.vecsesiot.backend.timetable

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TimetableService {
	@Autowired
	private lateinit var repository: TimetableRepository
}