package hu.vecsesiot.backend.security

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class RoleTests {
	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun anonym() = runBlocking {
		mockMvc.get("/users/register") {
			//with(httpBasic("user","password"))
		}.andExpect {
			status { isMethodNotAllowed() }
		}
		mockMvc.get("/") {
			//with(httpBasic("user","password"))
		}.andExpect {
			status { isUnauthorized() }
		}
	}

	@Test
	fun user() = runBlocking {
	}
}