package hu.vecsesiot.backend.email

import hu.vecsesiot.backend.bus.BusController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

@Service
class EmailService {
	@Autowired
	private lateinit var mailSender: JavaMailSender

	@Autowired
	private lateinit var templateEngine: SpringTemplateEngine

	private val logger = LoggerFactory.getLogger(EmailService::class.java)

	fun <T : Template> sendEmailTemplate(email: String, template: T) {
		val ctx = Context()
		var templateName: String? = null
		var subject: String? = null
		for (property in template::class.memberProperties) {
			property as KProperty1<Any, *>
			val value = property.get(template) as String

			when (property.name) {
				"templateName" -> templateName = value
				"subject" -> subject = value
				else -> ctx.setVariable(property.name, value)
			}
		}
		templateName!!
		subject!!

		val emailBody = templateEngine.process(templateName, ctx)
		sendEmail(email, subject, emailBody)
	}

	fun sendEmail(to: String, subject: String, body: String) {
		val message = mailSender.createMimeMessage()
		message.subject = subject

		val helper = MimeMessageHelper(message, true)
		helper.setFrom("alkfejl23@alkfejl2023.org")
		helper.setTo(to)
		helper.setText(body, true)

		try {
			mailSender.send(message)
			logger.info("Email sent")
		} catch (e: MailException) {
			// toString required: there is a throwable .error overload
			logger.error("Email send failed ({})", e.toString())
		}
	}
}