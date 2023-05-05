package hu.vecsesiot.backend.user

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.faultticket.FaultTicket
import jakarta.persistence.*
import java.io.Serializable

// TODO why does session need deserialization, does it mean out of sync state?
@Entity
class User(
	@Id
	@GeneratedValue
	var id: Long? = null,
	@Column(unique = true)
	var username: String,
	@Column(unique = true)
	var email: String,
	var name: String,
	var password: String,
	@ElementCollection(fetch = FetchType.EAGER)
	val roles: List<String>,
) : Serializable {
	@OneToOne(mappedBy = "user")
	val bus: Bus? = null

	@OneToMany(mappedBy = "user")
	lateinit var faultTickets: List<FaultTicket>
}
