package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.timetable.Timetable
import hu.vecsesiot.backend.user.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

// Bus represent a physical bus.
// Bus may be temporarily linked to a bus or even to a timetable.
@Entity
class Bus (
	@Id
	@GeneratedValue
	val id: Long,
	@NotBlank
	@Column(unique = true)
	val serialnumber: String,
	@OneToOne
	val user: User?,
	@OneToOne
	val timetable: Timetable?,
	@OneToMany(mappedBy = "bus")
	val faultTickets: List<FaultTicket>,
)
