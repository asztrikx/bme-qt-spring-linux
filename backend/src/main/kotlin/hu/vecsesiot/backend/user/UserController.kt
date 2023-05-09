package hu.vecsesiot.backend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController {
	@Autowired
	private lateinit var service: UserService

	@GetMapping("/register")
	fun register(@RequestBody registerDto: RegisterDto): ResponseEntity<Any> {
		return try {
			service.register(registerDto)
			ResponseEntity.ok().build()
		} catch (e: Exception) {
			ResponseEntity.status(HttpStatus.CONFLICT).build()
		}
	}
}