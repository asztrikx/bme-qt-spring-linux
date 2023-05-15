package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.section.Section
import hu.vecsesiot.backend.stop.GPSCoordinate
import hu.vecsesiot.backend.stop.Stop
import hu.vecsesiot.backend.timetable.Timetable
import hu.vecsesiot.backend.user.User
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
class Line(
	@Id
	@GeneratedValue
	var id: Long? = null,

	@get:NotBlank
	@get:Size(min = 4, max = 50)
	@Column(unique = true)
	var name: String,
) {
	@ManyToMany
	lateinit var route: MutableList<Section>

	@ManyToMany
	lateinit var stops: MutableList<Stop> // TODO How will rest fill this?

	@OneToMany(mappedBy = "line", cascade = [CascadeType.REMOVE])
	lateinit var timetable: MutableList<Timetable>

	@ManyToMany(mappedBy = "lineSubscriptions")
	lateinit var subscribedUsers: MutableList<User>
}
