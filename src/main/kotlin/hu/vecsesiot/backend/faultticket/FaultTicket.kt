package hu.vecsesiot.backend.faultticket

import hu.vecsesiot.backend.bus.Bus
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.user.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
class FaultTicket(
    @Id
    @GeneratedValue
    var id: Long? = null,
    var startDate: LocalDateTime,
    var resolveDate: LocalDateTime? = null,
    @NotBlank
    var description: String,
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
