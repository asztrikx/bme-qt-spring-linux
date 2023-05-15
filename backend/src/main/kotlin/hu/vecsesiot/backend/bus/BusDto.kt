package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.stop.GPSCoordinate

class BusDto(
	val id: Long? = null,
	val serialnumber: String,
	val coordinate: GPSCoordinate?,
)