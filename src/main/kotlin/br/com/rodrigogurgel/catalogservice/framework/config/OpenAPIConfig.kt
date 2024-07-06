package br.com.rodrigogurgel.catalogservice.framework.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val contact = Contact()
        contact.email = "rodrigohenrique.gurgel@outlook.com"
        contact.name = "Rodrigo Gurgel"
        contact.url = "https://www.linkedin.com/in/rodrigo-gurgel-9122b5140/"

        val mitLicense: License = License().name(
            "MIT License"
        ).url("https://github.com/rodrigogurgel/catalog-service/blob/main/LICENSE")

        val info: Info = Info()
            .title("Catalog Service API")
            .version("1.0")
            .contact(contact)
            .description("This API exposes endpoints to manage catalogs")
            .license(mitLicense)

        return OpenAPI().info(info)
    }
}
