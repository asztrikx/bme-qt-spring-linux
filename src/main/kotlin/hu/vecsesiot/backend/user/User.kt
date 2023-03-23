package hu.vecsesiot.backend.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class User (
	@Id
	@GeneratedValue
	private val id: Long?,
)