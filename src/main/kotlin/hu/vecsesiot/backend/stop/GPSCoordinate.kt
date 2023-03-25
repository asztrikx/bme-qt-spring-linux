package hu.vecsesiot.backend.stop

import jakarta.persistence.Embeddable

@Embeddable
class GPSCoordinate(
	val longitude: Double,
	val latitude: Double,
)