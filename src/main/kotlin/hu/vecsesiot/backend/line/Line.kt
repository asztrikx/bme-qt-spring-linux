package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.section.Section
import hu.vecsesiot.backend.stop.Stop
import hu.vecsesiot.backend.timetable.Timetable
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
class Line(
	@Id
	@GeneratedValue
	var id: Long? = null,

	// TODO determine max size
	@NotBlank
	@Size(min = 4, max = 50)
	var name: String,
) {
	@ManyToMany
	lateinit var route: List<Section>

	@ManyToMany
	lateinit var stops: List<Stop>

	@OneToMany(mappedBy = "line", cascade = [CascadeType.REMOVE])
	lateinit var timetable: List<Timetable>
}
