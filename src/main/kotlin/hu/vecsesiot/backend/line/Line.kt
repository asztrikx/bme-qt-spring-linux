package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.section.Section
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
class Line{
	@Id
	@GeneratedValue
	private var id: Long? = null

	@NotBlank
	@Size(min = 4, max = 50)
	private lateinit var name: String

	@ManyToMany
	private lateinit var route: List<Section>

	@OneToMany(mappedBy = "line", cascade = [CascadeType.REMOVE])
	private lateinit var timetable: List<Timetable>
}
