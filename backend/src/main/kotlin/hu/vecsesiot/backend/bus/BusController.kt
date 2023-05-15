package hu.vecsesiot.backend.bus

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
	fun takeTimetable(@PathVariable id : Long): ResponseEntity<Nothing> = try {
		busService.takeTimetable(id)
		ResponseEntity.ok().build()
	} catch (e: Exception) {
		ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
	}


	@PostMapping("/finish")
	fun finishTimetable(): ResponseEntity<Nothing> = try {
		busService.finishTimetable()
		ResponseEntity.ok().build()
	} catch (e: Exception) {
		ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
	}
}
