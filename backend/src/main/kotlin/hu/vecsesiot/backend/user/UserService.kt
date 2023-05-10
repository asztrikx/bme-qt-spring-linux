package hu.vecsesiot.backend.user

import hu.vecsesiot.backend.email.EmailService
import hu.vecsesiot.backend.security.UserToUserDetails
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService {
	@Autowired
	private lateinit var repository: UserRepository

	@Autowired
	private lateinit var emailService: EmailService

	@Autowired
	private lateinit var passwordEncoder: PasswordEncoder

	@Transactional
	fun register(registerDto: RegisterDto) {
		val user = repository.findUserByUsername(registerDto.username)
		check(user == null)

		repository.save(User(
			null,
			registerDto.username,
			registerDto.email,
			registerDto.name,
			passwordEncoder.encode(registerDto.password),
			listOf(),
		))

		//emailService.sendEmail(user.email, "Regisztráció", )
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