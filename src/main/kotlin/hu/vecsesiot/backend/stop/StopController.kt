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

    @GetMapping("/?name={:name}")
    fun getStopByName(@PathVariable name: String) = service.findStopsByName(name)
}