package hu.vecsesiot.backend.security

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
			// logout is accessible by default
			authorizeHttpRequests {
				// More generale rules here
				// TODO refine this
				authorize("/api/**", hasAuthority("User"))
				authorize("/**", hasAuthority("User"))
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