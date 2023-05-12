package hu.vecsesiot.backend.email

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RegisterTemplate(
	val name: String,
	val username: String,
	date: LocalDateTime,
) : Template("Registration", "Welcome to VecsesIot") {
	val date = date.format(
		DateTimeFormatter.ofPattern("yyyy/MM/dd")
	)
}
