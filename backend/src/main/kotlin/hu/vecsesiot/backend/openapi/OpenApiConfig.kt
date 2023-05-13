package hu.vecsesiot.backend.openapi

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// https://lemoncode21.medium.com/how-to-add-openapi-and-swagger-in-spring-boot-3-7c8d4dbc1f6e
@Configuration
@OpenAPIDefinition
class OpenApiConfig {
	@Bean
	fun openApi() = OpenAPI().apply {
		info = Info().apply {
			title = "VecsesIoT documentation"
			version = "1.0.0"
			description = ""
		}

	}
}