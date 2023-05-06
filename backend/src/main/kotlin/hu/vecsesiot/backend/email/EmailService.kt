package hu.vecsesiot.backend.email

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class EmailService {
	@Autowired
	private lateinit var mailSender: JavaMailSender
	fun sendEmail(to: String, subject: String, body: String) {
		val message = SimpleMailMessage()
		message.setTo(to)
		message.from = "alkfejl23@alkfejl2023.org"
		message.subject = subject
		message.text = body
		mailSender.send(message)
	}
}