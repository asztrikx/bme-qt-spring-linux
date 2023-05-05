package hu.vecsesiot.backend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController {
	// TODO dto for registration
	/*
	@Autowired
	private lateinit var passwordEncoder: PasswordEncoder
	passwordEncoder.encode("admin")
	*/
}