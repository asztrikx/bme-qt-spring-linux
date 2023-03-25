package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.user.User
import jakarta.persistence.*
import java.util.*

@Entity
class FaultTicket(
	@Id
	@GeneratedValue
	var id: Long?,
	var startDate: Date,
	var resolveDate: Date,
	var description: String,
	// could be a computed value
	var coordinate: GPSCoordinate,
	@Enumerated(EnumType.STRING)
	var state: State
) {
	@ManyToOne
	lateinit var user: User

	// could be a computed value
	@ManyToOne
	lateinit var bus: Bus

	enum class State {
		Created, InProgress, Resolved
	}
}
