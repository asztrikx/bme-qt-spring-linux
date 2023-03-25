package hu.vecsesiot.backend.stop

import hu.vecsesiot.backend.line.Line
import jakarta.persistence.*

@Entity
class Stop(
	@Id
	@GeneratedValue
	var id: Long?,
	var coordinate: GPSCoordinate,
	var name: String,
) {
	@ManyToMany(mappedBy = "stops")
	lateinit var lines: List<Line>
}
