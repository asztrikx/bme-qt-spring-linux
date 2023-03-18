package hu.vecsesiot.backend.line

import hu.vecsesiot.backend.section.Section
import hu.vecsesiot.backend.timetable.Timetable
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany

@Entity
class Line (
	@Id
	@GeneratedValue
	private val id: Long?,
	private val name: String,
	@ManyToMany
	private val route: List<Section>,
	@OneToMany(mappedBy = "line")
	private val timetable: List<Timetable>,
)
