package hu.vecsesiot.backend.bus

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/buses")
class BusController {
	@Autowired
	private lateinit var service: BusService
}
