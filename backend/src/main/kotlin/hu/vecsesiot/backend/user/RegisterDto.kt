package hu.vecsesiot.backend.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

class RegisterDto(
	@get:Email
	val email: String,
	@get:NotBlank
	val username: String,
	@get:NotBlank
	val password: String,
	@get:NotBlank
	val name: String,
)