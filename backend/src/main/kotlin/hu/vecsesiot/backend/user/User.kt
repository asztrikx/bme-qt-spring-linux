package hu.vecsesiot.backend.user

import com.fasterxml.jackson.annotation.JsonIgnore
import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.line.Line
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
	val roles: MutableList<String>,
) {
	@OneToOne(mappedBy = "user")
	val bus: Bus? = null

	@OneToMany(mappedBy = "user")
	lateinit var faultTickets: MutableList<FaultTicket>

	@ManyToMany
	lateinit var lineSubscriptions: MutableList<Line>
}
