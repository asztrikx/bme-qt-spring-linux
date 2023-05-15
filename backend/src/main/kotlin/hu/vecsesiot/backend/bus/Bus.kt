package hu.vecsesiot.backend.bus

import hu.vecsesiot.backend.faultticket.FaultTicket
import hu.vecsesiot.backend.section.Section
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

			// Get section we are in
			var travelTimeAtEndOfSection = Duration.ZERO
			var currentSection: Section? = null
			for (section in line.route) {
				travelTimeAtEndOfSection += section.timespan
				if (travelTimeAtEndOfSection >= travelTime) {
					currentSection = section
					break
				}
			}
			currentSection!!
			val travelTimeAtStartOfSection = travelTimeAtEndOfSection.minus(currentSection.timespan)

			// Fix traveltime (travel isn't started yet or travel is already over)
			travelTime = min(travelTime, Duration.ZERO)
			travelTime = max(travelTime, travelTimeAtEndOfSection)

			// Get distance in subsection
			var distanceOfSection = 0.0
			for (subsection in currentSection.sectionPoints.zipWithNext()) {
				distanceOfSection += subsection.length()
			}

			val ratioInsideSection = (travelTime - travelTimeAtStartOfSection).seconds.toDouble() / (travelTimeAtEndOfSection - travelTimeAtStartOfSection).seconds
			val distanceInSection = distanceOfSection * ratioInsideSection

			// Get subsection we are in
			var distanceAtEndOfSubsection = 0.0
			var currentSubsection: Pair<GPSCoordinate, GPSCoordinate>? = null
			for (subsection in currentSection.sectionPoints.zipWithNext()) {
				val vector = subsection.second - subsection.first
				distanceAtEndOfSubsection += vector.length()

				if (distanceAtEndOfSubsection > distanceInSection) {
					currentSubsection = subsection
					break
				}
			}
			currentSubsection!!

			// Get gps inside subsection
			val distanceAtStartOfSubSection = distanceAtEndOfSubsection - currentSubsection.length()
			val ratioInsideSubSection = (distanceInSection - distanceAtStartOfSubSection) / (distanceAtEndOfSubsection - distanceAtStartOfSubSection)
			return currentSubsection.first + (currentSubsection.second - currentSubsection.first) * ratioInsideSubSection
		}

	fun max(a: Duration, b: Duration) = if (a >= b) a else b
	fun min(a: Duration, b: Duration) = if (a >= b) b else a

	fun Pair<GPSCoordinate, GPSCoordinate>.length(): Double {
		val vector = second - first
		return vector.length()
	}
}
