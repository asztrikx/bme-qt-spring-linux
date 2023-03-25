package hu.vecsesiot.backend.timetable

import hu.vecsesiot.backend.line.Line
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
class Timetable(
	@Id
	@GeneratedValue
	var id: Long? = null,

	@ManyToOne
	var line: Line,

	var startDate: LocalDateTime,
)
