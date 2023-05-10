package hu.vecsesiot.backend.email

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver

@Configuration
class TemplateEngineConfig {
    @Bean
    fun templateEngine(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.addTemplateResolver(templateResolver())
        return templateEngine
    }

    private fun templateResolver(): ITemplateResolver {
        val resolver = ClassLoaderTemplateResolver()
        resolver.setPrefix("templates/email/")
        resolver.setSuffix(".html")
        resolver.setTemplateMode("HTML5")
        resolver.setOrder(1)
        resolver.setCacheable(true)
        return resolver
    }
}