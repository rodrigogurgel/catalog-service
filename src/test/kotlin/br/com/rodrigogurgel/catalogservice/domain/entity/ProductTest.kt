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
        product.id.value shouldBe id.value
        product.name shouldBe name
        product.description?.value shouldBe description.value
        product.image shouldBe image
        product.image?.path shouldBe image.path
    }

    @Test
    fun `Should instantiate Product with success with optional values null`() {
        val id = Id(UUID.randomUUID())
        val name = Name(randomString(30))
        val description = null
        val image = null
        val product = Product(
            id = id,
            name = name,
            description = description,
            image = image
        )

        product.id shouldBe id
        product.id.value shouldBe id.value
        product.name shouldBe name
        product.description shouldBe null
        product.image shouldBe image
        product.image shouldBe null
    }
}
