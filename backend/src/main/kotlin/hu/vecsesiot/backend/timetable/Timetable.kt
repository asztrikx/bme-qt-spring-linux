package hu.vecsesiot.backend.timetable

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.line.Line
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import java.time.LocalDateTime

@Entity
class Timetable(
	@Id
	@GeneratedValue
	var id: Long? = null,

	var startDate: LocalDateTime,
) {
	@ManyToOne
	lateinit var line: Line

	@OneToOne(mappedBy = "timetable")
	var bus: Bus? = null
}
