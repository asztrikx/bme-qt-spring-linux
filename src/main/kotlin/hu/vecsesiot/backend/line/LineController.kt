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
@RequestMapping("/lines")
class LineController {

    @Autowired
    private lateinit var service: LineService

    @PostMapping("")
    fun addLine(@RequestBody line: Line) = service.addLine(line)

    @GetMapping("/{:id}")
    fun getLineById(@PathVariable id: Long) = service.findById(id)

    @GetMapping("/line?name={:name}")
    fun getLineByName(@PathVariable name: String) = service.findLinesByName(name)

    @DeleteMapping("/{:id}")
    fun deleteLineById(@PathVariable id: Long) = service.deleteLineById(id)
}