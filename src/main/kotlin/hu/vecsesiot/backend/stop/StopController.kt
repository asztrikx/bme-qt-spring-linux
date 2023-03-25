package hu.vecsesiot.backend.stop


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Suppress("IMPLICIT_CAST_TO_ANY")
@RestController
@RequestMapping("/Stops")
class StopController {

    @Autowired
    private lateinit var service: StopService

    @PostMapping("")
    fun addStop(@RequestBody stop: Stop) = service.addStop(stop)

    @GetMapping("/{:id}")
    fun getStopById(@PathVariable id: Long) = service.findById(id)

    @GetMapping("/?name={:name}")
    fun getStopByName(@PathVariable name: String) = service.findStopsByName(name)

    @PutMapping("")
    fun updateStop(@RequestBody stop: Stop) = if (stop.id == null) ResponseEntity.notFound() else service.addStop(stop)

    @DeleteMapping("/{:id}")
    fun deleteStopById(@PathVariable id: Long) = service.deleteStopById(id)
}