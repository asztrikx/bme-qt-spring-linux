package hu.vecsesiot.backend.section

import hu.vecsesiot.backend.stop.Stop
import java.time.Duration

class Section (
	private val start: Stop,
	private val stop: Stop,
	private val timespan: Duration,
)
