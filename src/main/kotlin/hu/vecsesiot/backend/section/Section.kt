package hu.vecsesiot.backend.section

import hu.vecsesiot.backend.stop.Stop
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.hibernate.validator.constraints.time.DurationMin
import java.time.Duration

@Entity
class Section (
	@Id
	@GeneratedValue
	val id: Long,
	@ManyToOne
	val start: Stop,
	@ManyToOne
	val stop: Stop,
	// Hibernate solves the mapping to SQL primitives without using @Converter
	@DurationMin(seconds = 1)
	val timespan: Duration,
)
