package hu.vecsesiot.backend.bus

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buses")
class BusController {
	@Autowired
	private lateinit var busService: BusService

	@PostMapping("/take/{id}")
	fun takeTimetable(@PathVariable id : Long) = busService.takeTimetable(id)

	@PostMapping("/finish")
	fun finishTimetable() = busService.finishTimetable()
}
