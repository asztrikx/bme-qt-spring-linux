package hu.vecsesiot.backend.user

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService {
	@Autowired
	private lateinit var repository: UserRepository

	@Autowired
	private lateinit var passwordEncoder: PasswordEncoder

	@Transactional
	fun register(registerDto: RegisterDto) {
		val user = repository.findUserByUsername(registerDto.username)
		check(user != null)

		repository.save(User(
			null,
			registerDto.username,
			registerDto.email,
			registerDto.name,
			passwordEncoder.encode(registerDto.password),
			listOf(),
		))
	}
}