package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.section.Section
import hu.vecsesiot.backend.stop.Stop
import hu.vecsesiot.backend.timetable.Timetable
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Entity
class Line(
	@Id
	@GeneratedValue
	var id: Long? = null,

	@NotBlank
	@Size(min = 4, max = 50)
	var name: String,

	@ManyToMany
	var route: List<Section>,

	@ManyToMany
	var stops: List<Stop>,

	@OneToMany(mappedBy = "line", cascade = [CascadeType.REMOVE])
	var timetable: List<Timetable>,
)
