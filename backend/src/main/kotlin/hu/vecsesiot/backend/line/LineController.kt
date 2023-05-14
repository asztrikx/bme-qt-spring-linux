package hu.vecsesiot.backend.line


import hu.vecsesiot.backend.bus.Bus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/lines")
class LineController {
	@Autowired
	private lateinit var service: LineService

	@GetMapping("/{id}/nextbus/{stopid}")
	fun getNextOperationalBusBeforeStop(@PathVariable id: Long, @PathVariable stopid: Long): ResponseEntity<Bus> {
		val bus = try {
			service.getNextOperationalBusBeforeStop(id, stopid, LocalDateTime.now())
		} catch (e: Exception) {
			return ResponseEntity.badRequest().build()
		}

		return ResponseEntity.ofNullable(bus)
	}

	@GetMapping("/{id}/nextbustime/{stopid}")
	fun getNextOperationalBusTimeBeforeStop(@PathVariable id: Long, @PathVariable stopid: Long): ResponseEntity<LocalDateTime> {
		val time = try {
			service.getNextOperationalBusTimeBeforeStop(id, stopid, LocalDateTime.now())
		} catch (e: Exception) {
			return ResponseEntity.badRequest().build()
		}

		return ResponseEntity.ofNullable(time)
	}


	@GetMapping("/{id}/brokenbuses/{stopid}")
	fun getAllBrokenBusBeforeStop(@PathVariable id: Long, @PathVariable stopid: Long): ResponseEntity<List<Bus>> {
		val buses = try {
			service.getAllBrokenBusBeforeStop(id, stopid, LocalDateTime.now())
		} catch (e: Exception) {
			return ResponseEntity.badRequest().build()
		}

		return ResponseEntity.ofNullable(buses)
	}
}
