package hu.vecsesiot.backend.security

import hu.vecsesiot.backend.security.RoleType.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
	// https://stackoverflow.com/a/72392539/4404911
	@Autowired
	private lateinit var jpaUserDetailsService: JpaUserDetailsService

	@Bean
	fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
		authenticationConfiguration.authenticationManager

	@Bean
	fun bCryptPasswordEncoder() = BCryptPasswordEncoder()

	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		// https://www.baeldung.com/kotlin/spring-security-dsl
		http {
			authorizeHttpRequests {
				// All Driver has a User role
				// All Maintenance has a User role

				// The following config basically tells the servlet to hide /api from us,
				// so we have to specify urls without it otherwise it becomes /api/api on a client.
				// server.servlet.context-path: /api

				authorize("/users/register", permitAll)
				authorize("/users/details", hasAnyAuthority(User))
				authorize("/users/subscribe/**", hasAnyAuthority(User))
				authorize("/users/unsubscribe/**", hasAnyAuthority(User))
				authorize("/buses/**", hasAnyAuthority(Driver))
				authorize("/faultTickets/**", hasAnyAuthority(Driver, Maintenance))
				authorize("/timetables/**", hasAnyAuthority(User))
				authorize("/sections/**", hasAnyAuthority(User))
				authorize("/lines/**", hasAnyAuthority(User))
				authorize("/stops/**", hasAnyAuthority(User))
				// Prevent infinite redirects to /error because of BadCredentialException which redirects to /error
				authorize("/error", permitAll)
				authorize("/", hasAnyAuthority(User))
				authorize(anyRequest, hasAnyAuthority(Developer))
			}
			cors {
				disable()
			}
			csrf {
				disable()
			}
			httpBasic { }
		}
		return http.build()
	}
}