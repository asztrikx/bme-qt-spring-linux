package hu.vecsesiot.backend.stop

import jakarta.persistence.Embeddable

@Embeddable
class GPSCoordinate(
	var latitude: Double,
	var longitude: Double,
) {
	operator fun plus(gpsCoordinate: GPSCoordinate): GPSCoordinate {
		return GPSCoordinate(latitude + gpsCoordinate.latitude, longitude + gpsCoordinate.longitude)
	}

	operator fun minus(gpsCoordinate: GPSCoordinate): GPSCoordinate {
		return GPSCoordinate(latitude - gpsCoordinate.latitude, longitude - gpsCoordinate.longitude)
	}

	operator fun times(d: Double): GPSCoordinate {
		return GPSCoordinate(latitude * d, longitude * d)
	}
}