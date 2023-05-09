package hu.vecsesiot.backend.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController {

	@Autowired
	private lateinit var userService: UserService


	// TODO dto for registration
	/*
	@Autowired
	private lateinit var passwordEncoder: PasswordEncoder
	passwordEncoder.encode("admin")
	*/
}