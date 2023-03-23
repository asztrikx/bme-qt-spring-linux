package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.timetable.Timetable
import hu.vecsesiot.backend.user.User
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Entity

// TODO explain temporary values
@Entity
class Bus (
	@Id
	@GeneratedValue
	private val id: Long,
	private val model: String,

	@OneToOne
	private val user: User?,
	@OneToOne
	private val timetable: Timetable?,
)
