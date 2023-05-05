package hu.vecsesiot.backend.timetable

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/timetables")
class TimetableController {
	@Autowired
	private lateinit var service: TimetableService
}