package hu.vecsesiot.backend.user

import com.fasterxml.jackson.annotation.JsonIgnore
import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.faultticket.FaultTicket
import jakarta.persistence.*

@Entity
class User(
	@Id
	@GeneratedValue
	var id: Long? = null,
	@Column(unique = true)
	var username: String,
	@JsonIgnore
	var email: String,
	@JsonIgnore
	var name: String,
	@JsonIgnore
	var password: String,
	@JsonIgnore
	@ElementCollection(fetch = FetchType.EAGER)
	val roles: List<String>,
) {
	@OneToOne(mappedBy = "user")
	val bus: Bus? = null

	@OneToMany(mappedBy = "user")
	lateinit var faultTickets: List<FaultTicket>

	@ManyToMany
	lateinit var lineSubscriptions: List<User>
}
