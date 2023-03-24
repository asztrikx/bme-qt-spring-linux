package hu.vecsesiot.backend.section

import hu.vecsesiot.backend.stop.Stop
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.Duration

@Entity
class Section (
	@ManyToOne
	private val start: Stop,
	@ManyToOne
	private val stop: Stop,
	private val timespan: Duration,
)
