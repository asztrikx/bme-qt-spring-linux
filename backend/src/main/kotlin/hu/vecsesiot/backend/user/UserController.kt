package hu.vecsesiot.backend.user

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {
	@Autowired
	private lateinit var service: UserService

	@PostMapping("/register")
	fun register(@Valid @RequestBody registerDto: RegisterDto): ResponseEntity<Nothing> {
		return try {
			service.register(registerDto)
			ResponseEntity.ok().build()
		} catch (e: Exception) {
			ResponseEntity.status(HttpStatus.CONFLICT).build()
		}
	}

	@GetMapping("/details")
	fun details() = service.details()

	@PostMapping("/subscribe/{id}")
	fun subscribeForLine(@PathVariable id: Long) = service.subscribeForLine(id)
}