package hu.vecsesiot.backend.stop

import jakarta.persistence.Embeddable

@Embeddable
class GPSCoordinate(
	var longitude: Double,
	var latitude: Double,
)