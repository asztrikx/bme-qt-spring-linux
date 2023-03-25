package hu.vecsesiot.backend.timetable

import hu.vecsesiot.backend.line.Line
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/timetables")
class TimetableController {
	@Autowired
	private lateinit var service: TimetableService
}