package hu.vecsesiot.backend.line


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lines")
class LineController {
	@Autowired
	private lateinit var service: LineService
}