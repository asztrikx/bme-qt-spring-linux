package hu.vecsesiot.backend.email

import hu.vecsesiot.backend.bus.BusController
import hu.vecsesiot.backend.user.RegisterDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class EmailService {
	@Autowired
	private lateinit var mailSender: JavaMailSender

	@Autowired
	private lateinit var templateEngine: SpringTemplateEngine

	private val logger = LoggerFactory.getLogger(BusController::class.java)

	fun sendRegistrationWelcomeEmail(user: RegisterDto) {
		val myContext = Context()
		myContext.setVariable("name", user.name)
		myContext.setVariable("username", user.username)
		myContext.setVariable(
			"date", LocalDateTime.now().format(
				DateTimeFormatter.ofPattern("yyyy/MM/dd")
			)
		)

		val emailBody: String = templateEngine.process("Registration", myContext)

		sendEmail(user.email, "Welcome to VecsesIot", emailBody)
	}

	fun sendFaultNotificationEmail(email: String, user: String, line: String) {
		val myContext = Context()
		myContext.setVariable("name", user)
		myContext.setVariable("line", line)

		val emailBody: String = templateEngine.process("FaultNotification", myContext)

		sendEmail(email, "Bus broke down in your way", emailBody)
	}

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