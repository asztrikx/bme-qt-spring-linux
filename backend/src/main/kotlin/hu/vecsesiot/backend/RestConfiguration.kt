package hu.vecsesiot.backend

import hu.vecsesiot.backend.line.Line
import hu.vecsesiot.backend.stop.Stop
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry

@Configuration
class RestConfiguration : RepositoryRestConfigurer {
	override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration, cors: CorsRegistry) {
		config.exposeIdsFor(Stop::class.java)
		config.exposeIdsFor(Line::class.java)
	}
}