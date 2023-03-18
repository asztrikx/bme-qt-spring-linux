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
	private val id: Long?,
	private val user: User,
	// could be a computed value
	private val bus: Bus,
	private val startDate: Date,
	private val resolveDate: Date,
	private val description: String,
	// could be a computed value
	private val coordinate: GPSCoordinate,
	@Enumerated(EnumType.STRING)
	private val state: State
)

enum class State {
	Created, InProgress, Resolved
}