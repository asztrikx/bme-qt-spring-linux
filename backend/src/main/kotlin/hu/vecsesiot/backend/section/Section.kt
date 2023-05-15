package hu.vecsesiot.backend.section

import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.stop.Stop
import jakarta.persistence.*
import org.hibernate.validator.constraints.time.DurationMin
import java.time.Duration

@Entity
class Section(
	@Id
	@GeneratedValue
	var id: Long? = null,

	// Hibernate solves the mapping to SQL primitives without using @Converter
	// Disallow negative duration
	@get:DurationMin(seconds = 1)
	var timespan: Duration,
) {
	@ManyToOne
	lateinit var start: Stop // TODO How will rest fill this?

	@ManyToOne
	lateinit var stop: Stop // TODO How will rest fill this?

	@ElementCollection(fetch = FetchType.EAGER)
	lateinit var sectionPoints: MutableList<GPSCoordinate>
}
