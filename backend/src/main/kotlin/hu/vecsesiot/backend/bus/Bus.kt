package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.timetable.Timetable
import hu.vecsesiot.backend.user.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.time.Duration
import java.time.LocalDateTime

// Bus represent a physical bus.
// Bus may be temporarily linked to a bus or even to a timetable.
@Entity
class Bus(
	@Id
	@GeneratedValue
	var id: Long? = null,
	@get:NotBlank
	@Column(unique = true)
	var serialnumber: String,
) {
	@OneToOne
	var user: User? = null

	@OneToOne
	var timetable: Timetable? = null

	@OneToMany(mappedBy = "bus")
	lateinit var faultTickets: MutableList<FaultTicket>

	val coordinate: GPSCoordinate?
		get() {
			val timetable = this.timetable
			timetable ?: return null
			val line = timetable.line

			var travelTime = Duration.between(timetable.startDate, LocalDateTime.now())

			var travelTimeAtEndOfSection = Duration.ZERO
			var currentSection = line.route.last()
			for (section in line.route) {
				travelTimeAtEndOfSection += section.timespan
				if (travelTimeAtEndOfSection > travelTime) {
					currentSection = section
					break
				}
			}
			val travelTimeAtSectionStart = travelTimeAtEndOfSection.minus(currentSection.timespan)

			travelTime = min(travelTime, Duration.ZERO)
			travelTime = max(travelTime, travelTimeAtEndOfSection)
			val ratioInsideSection = (travelTime - travelTimeAtSectionStart).seconds.toDouble() / (travelTimeAtEndOfSection - travelTimeAtSectionStart).seconds
			return currentSection.start.coordinate + (currentSection.stop.coordinate - currentSection.start.coordinate) * ratioInsideSection
		}

	fun max(a: Duration, b: Duration) = if (a >= b) a else b
	fun min(a: Duration, b: Duration) = if (a >= b) b else a
}
