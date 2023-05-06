package hu.vecsesiot.backend.stop

import jakarta.persistence.Embeddable

@Embeddable
class GPSCoordinate(
        var latitude: Double,
        var longitude: Double,
)