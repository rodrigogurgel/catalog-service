package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StoreTest {
    @Test
    fun `Should instantiate Store with success`() {
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(100))
        val categories = mutableMapOf<Id, Category>()
        val products = mutableMapOf<Id, Product>()
        val store = Store(id, name, description, categories, products)

        assertEquals(id, store.id)
        assertEquals(name, store.name)
        assertEquals(description, store.description)
        assertEquals(categories, store.categories)
        assertEquals(products, store.products)
    }
}
