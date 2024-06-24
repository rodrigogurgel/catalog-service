package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Image
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

class ProductTest {
    @Test
    fun `Should instantiate Product with success`() {
        val id = Id(UUID.randomUUID())
        val name = Name(randomString(30))
        val description = Description(randomString(30))
        val image = Image("https://www.${randomString(10)}.com")
        val product = Product(
            id = id,
            name = name,
            description = description,
            image = image
        )

        product.id shouldBe id
        product.id.id shouldBe id.id
        product.name shouldBe name
        product.description shouldBe description
        product.image shouldBe image
    }
}
