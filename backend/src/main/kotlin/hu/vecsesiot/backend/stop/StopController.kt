package hu.vecsesiot.backend.stop


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stops")
class StopController {
	@Autowired
	private lateinit var service: StopService
}