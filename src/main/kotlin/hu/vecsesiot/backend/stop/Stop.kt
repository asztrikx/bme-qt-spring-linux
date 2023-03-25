package hu.vecsesiot.backend.stop

import hu.vecsesiot.backend.line.Line
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class Stop(
	@Id
	@GeneratedValue
	val id: Long?,
	val coordinate: GPSCoordinate,
	val name : String,
	// https://stackoverflow.com/a/14111651/4404911
	@ManyToMany(mappedBy="stops")
	val lines : List<Line>
)
