package hu.vecsesiot.backend.user

import hu.vecsesiot.backend.email.EmailService
import hu.vecsesiot.backend.email.RegisterTemplate
import hu.vecsesiot.backend.line.LineRepository
import hu.vecsesiot.backend.security.RoleType
import hu.vecsesiot.backend.security.UserToUserDetails
import hu.vecsesiot.backend.security.expandRoles
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull


@Service
class UserService {
	@Autowired
	private lateinit var repository: UserRepository

	@Autowired
	private lateinit var lineRepository : LineRepository

	@Autowired
	private lateinit var emailService: EmailService

	@Autowired
	private lateinit var passwordEncoder: PasswordEncoder

	@Transactional
	fun subscribeForLine(id: Long){
		val authentication = SecurityContextHolder.getContext().authentication
		val principal = authentication.principal as UserToUserDetails
		val user = repository.findById(principal.id).getOrNull()
		val line = lineRepository.findById(id).getOrNull()

		check(user != null)
		check(line != null)
		check(!user.lineSubscriptions.contains(line))

		user.lineSubscriptions.add(line)
		repository.save(user)
	}

	@Transactional
	fun unsubscribeFromLine(id: Long){
		val authentication = SecurityContextHolder.getContext().authentication
		val principal = authentication.principal as UserToUserDetails
		val user = repository.findById(principal.id).getOrNull()
		val line = lineRepository.findById(id).getOrNull()

		check(user != null)
		check(line != null)
		check(user.lineSubscriptions.contains(line))

		user.lineSubscriptions.remove(line)
		repository.save(user)
	}

	/**
	 * [Transactional] is also good for email sending failure
	 */
	@Transactional
	fun register(registerDto: RegisterDto) {
		val user = repository.findUserByUsername(registerDto.username)
		check(user == null)

		repository.save(
			User(
				null,
				registerDto.username,
				registerDto.email,
				registerDto.name,
				passwordEncoder.encode(registerDto.password),
				expandRoles(RoleType.User),
			)
		)

		emailService.sendEmailTemplate(
			registerDto.email,
			RegisterTemplate(
				registerDto.name,
				registerDto.username,
				LocalDateTime.now(),
			)
		)
	}

	@Transactional
	fun details(): DetailsDto {
		val authentication = SecurityContextHolder.getContext().authentication
		val principal = authentication.principal as UserToUserDetails

		return DetailsDto(
			principal.id,
			principal.authorities.map { it.authority },
		)
	}
}