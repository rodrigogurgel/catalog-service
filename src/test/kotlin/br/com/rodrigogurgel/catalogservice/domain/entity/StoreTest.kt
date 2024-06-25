package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategory
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class StoreTest {
    @Test
    fun `Should instantiate Store with success`() {
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(100))
        val category = mockCategory()
        val categories = mutableMapOf(category.id to category)
        val product = mockProduct()
        val products = mutableMapOf(product.id to product)
        val store = Store(id, name, description, categories, products)

        store.id shouldBe id
        store.name shouldBe name
        store.description shouldBe description
        store.categories shouldBe categories
        store.products shouldBe products
    }
}
