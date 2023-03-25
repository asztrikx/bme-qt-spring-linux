package hu.vecsesiot.backend

import hu.vecsesiot.backend.user.User
import hu.vecsesiot.backend.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class SqlInitializer : ApplicationRunner {
	@Autowired
	private lateinit var userRepository: UserRepository

	override fun run(args: ApplicationArguments) {
		val user = User(username = "asd")
		userRepository.save(user)
	}
}