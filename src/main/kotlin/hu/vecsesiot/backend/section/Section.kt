package hu.vecsesiot.backend.section

import hu.vecsesiot.backend.stop.Stop
import jakarta.persistence.Entity
import java.time.Duration

@Entity
class Section (
	private val start: Stop,
	private val stop: Stop,
	private val timespan: Duration,
)
