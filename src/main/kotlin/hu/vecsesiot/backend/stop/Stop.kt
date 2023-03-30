package hu.vecsesiot.backend.stop

import hu.vecsesiot.backend.line.Line
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
class Stop(
    @Id
    @GeneratedValue
    var id: Long? = null,
    var coordinate: GPSCoordinate,
    @NotBlank
    var name: String,
) {
    @ManyToMany(mappedBy = "stops")
    lateinit var lines: List<Line>
}
