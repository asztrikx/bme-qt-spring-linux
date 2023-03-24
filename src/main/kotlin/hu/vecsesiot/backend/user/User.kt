package hu.vecsesiot.backend.user

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.faultticket.FaultTicket
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne

@Entity
class User (
	@Id
	@GeneratedValue
	val id: Long?,
	@OneToOne(mappedBy = "user")
	val bus: Bus?,
	@OneToMany(mappedBy = "user")
	val faultTickets: List<FaultTicket>
)