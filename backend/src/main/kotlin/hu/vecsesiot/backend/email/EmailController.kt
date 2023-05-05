package hu.vecsesiot.backend.email

import com.sun.mail.iap.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EmailController {

    @Autowired
    private lateinit var service: EmailService

    @PostMapping("/email")
    public fun sendEmail(@RequestBody address: String) {
        service.sendEmail(address, "Test", "Hello VecsesIOT from Spring booot!")
    }
}