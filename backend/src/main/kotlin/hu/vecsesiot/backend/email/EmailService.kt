package hu.vecsesiot.backend.email

import hu.vecsesiot.backend.bus.BusController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService {
	@Autowired
	private lateinit var mailSender: JavaMailSender

	private val logger = LoggerFactory.getLogger(BusController::class.java)

	fun sendEmail(to: String, subject: String, body: String) {
		val message = SimpleMailMessage()
		message.setTo(to)
		message.from = "alkfejl23@alkfejl2023.org"
		message.subject = subject
		message.text = body

		try {
			mailSender.send(message)
			logger.info("Email sent")
		} catch (e: MailException) {
			// toString required: there is a throwable .error overload
			logger.error("Email send failed ({})", e.toString())
		}
	}
}