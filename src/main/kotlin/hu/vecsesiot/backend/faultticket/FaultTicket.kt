package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.user.User
import jakarta.persistence.*
import java.util.*

@Entity
class FaultTicket (
	@Id
	@GeneratedValue
	val id: Long?,
	@ManyToOne
	val user: User,
	// could be a computed value
	@ManyToOne
	val bus: Bus,
	val startDate: Date,
	val resolveDate: Date,
	val description: String,
	// could be a computed value
	val coordinate: GPSCoordinate,
	@Enumerated(EnumType.STRING)
	val state: State
)

enum class State {
	Created, InProgress, Resolved
}