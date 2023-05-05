package hu.vecsesiot.backend.section

import hu.vecsesiot.backend.stop.Stop
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.hibernate.validator.constraints.time.DurationMin
import java.time.Duration

@Entity
class Section(
	@Id
	@GeneratedValue
	var id: Long? = null,
	// Hibernate solves the mapping to SQL primitives without using @Converter
	// Disallow negative duration
	@DurationMin(seconds = 1)
	var timespan: Duration,
) {
	@ManyToOne
	lateinit var start: Stop

	@ManyToOne
	lateinit var stop: Stop
}
