package hu.vecsesiot.backend.user

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

class User (
	@Id
	@GeneratedValue
	private val id: Long?,
)