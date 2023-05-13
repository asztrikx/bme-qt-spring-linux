package hu.vecsesiot.backend.security

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class RoleTests {
	@Autowired
	lateinit var mockMvc: MockMvc

	inner class StatusBuilder(val cred: Pair<String, String>?, val status: HttpStatus) {
		private val urls = mutableListOf<String>()

		operator fun String.unaryPlus() {
			urls.add(this)
		}

		fun build() = runBlocking {
			urls.forEach { url ->
				mockMvc.get(url) {
					cred?.let {
						with(httpBasic(cred.first, cred.second))
					}
				}.andExpect {
					status {
						isEqualTo(status.value())
					}
				}
			}
		}
	}

	inner class CredBuilder(val cred: Pair<String, String>?) {
		fun status(status: HttpStatus, block: StatusBuilder.() -> Unit) {
			StatusBuilder(cred, status).apply(block).build()
		}
	}

	fun Pair<String, String>?.login(actions: CredBuilder.() -> Unit) {
		CredBuilder(this).apply(actions)
	}

	@Test
	fun anonym() {
		null.login {
			status(METHOD_NOT_ALLOWED) {
				+"/users/register"
			}
			status(INTERNAL_SERVER_ERROR) {
				+"/error"
			}
			status(UNAUTHORIZED) {
				+"/"
			}
		}
	}

	@Test
	fun user() {
		Pair("user", "123").login {
			status(OK) {
				+"/"
				+"/users/details"
			}
			status(FORBIDDEN) {
				+"/email"
				+"/buses"
				+"/faultTickets"
			}
		}
	}

	@Test
	fun maint() {
		Pair("main", "123").login {
			status(OK) {
				+"/"
				+"/users/details"
				+"/faultTickets"
			}
			status(FORBIDDEN) {
				+"/buses"
				+"/email"
			}
		}
	}

	@Test
	fun driver() {
		Pair("driver", "123").login {
			status(OK) {
				+"/"
				+"/users/details"
				+"/buses"
				+"/faultTickets"
			}
			status(FORBIDDEN) {
				+"/email"
			}
		}
	}

	@Test
	fun dev() {
		Pair("dev", "123").login {
			status(OK) {
				+"/"
				+"/users/details"
				+"/buses"
				+"/faultTickets"
			}
			status(METHOD_NOT_ALLOWED) {
				+"/email"
			}
		}
	}
}