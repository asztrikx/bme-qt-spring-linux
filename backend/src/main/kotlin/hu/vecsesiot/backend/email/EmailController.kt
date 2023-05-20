package hu.vecsesiot.backend.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController {
	@Autowired
	private lateinit var service: EmailService

	@PostMapping("/email")
	fun sendEmail(@RequestBody address: String) {
		service.sendEmail(address, "Test", "Hello VecsesIOT from Spring booot!")
	}

	@PostMapping("/emailTemplate")
	fun sendEmailTemplate(@RequestBody address: String) {
		service.sendEmailTemplate(address, FaultNotificationTemplate("Nagy István", "Funny ride"))
	}
}