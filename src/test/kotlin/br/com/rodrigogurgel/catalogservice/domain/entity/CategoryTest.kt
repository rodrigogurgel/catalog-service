package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CategoryTest {
    @Test
    fun `Should instantiate Category with success`() {
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(100))
        val status = Status.AVAILABLE
        val offers = mutableMapOf<Id, Offer>()
        val category = Category(
            id,
            name,
            description,
            status,
            offers
        )

        assertEquals(id, category.id)
        assertEquals(name, category.name)
        assertEquals(description, category.description)
        assertEquals(status, category.status)
        assertEquals(offers, category.offers)
    }
}
