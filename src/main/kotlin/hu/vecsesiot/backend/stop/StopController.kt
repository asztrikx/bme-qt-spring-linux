package hu.vecsesiot.backend.line


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/Stops")
class StopController {

    @Autowired
    private lateinit var service: StopService

    @PostMapping("")
    fun addStop(@RequestBody stop : Stop) = service.addStop(stop)

    @GetMapping("/{:id}")
    fun getStopById(@PathVariable id: Long) = service.findById(id)

    @GetMapping("/?name={:name}")
    fun getStopByName(@RequestBody name : String) = service.findStopsByName(name)

    @PutMapping("")
    fun updateStop(@RequestBody stop : Stop) = service.updateStop(stop)

    @DeleteMapping("/{:id}")
    fun deleteStopById(@PathVariable id: Long) = service.deleteStopById(id)
}