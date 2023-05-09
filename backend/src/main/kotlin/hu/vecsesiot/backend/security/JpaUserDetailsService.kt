package hu.vecsesiot.backend.security

import hu.vecsesiot.backend.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JpaUserDetailsService : UserDetailsService {
	@Autowired
	private lateinit var userRepository: UserRepository

	override fun loadUserByUsername(username: String): UserDetails {
		val user = userRepository.findUserByUsername(username)
		user ?: throw UsernameNotFoundException(username)
		return UserToUserDetails(user)
	}
}