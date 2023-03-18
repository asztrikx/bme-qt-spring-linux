package hu.vecsesiot.backend.stop

import jakarta.persistence.Embeddable

@Embeddable
class GPSCoordinate (
	private val longitude : Double,
	private val latitude : Double,
)