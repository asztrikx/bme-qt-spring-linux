package hu.vecsesiot.backend.user

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.faultticket.FaultTicket
import jakarta.persistence.*

@Entity
class User(
	@Id
	@GeneratedValue
	val id: Long? = null,
	val username: String,
) {
	@OneToOne(mappedBy = "user")
	val bus: Bus? = null

	@OneToMany(mappedBy = "user")
	lateinit var faultTickets: List<FaultTicket>
}