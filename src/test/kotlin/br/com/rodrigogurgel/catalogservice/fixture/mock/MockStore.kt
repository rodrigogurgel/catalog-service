package br.com.rodrigogurgel.catalogservice.fixture.mock

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.entity.Store
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.randomString

data class MockStore(
    var id: Id = Id(),
    var name: Name = Name(randomString(30)),
    var description: Description? = Description(randomString(1000)),
    var categories: MutableMap<Id, Category> = mutableMapOf(),
    var products: MutableMap<Id, Product> = mutableMapOf(),
)

fun mockStore(): Store = MockStore().run {
    Store(id, name, description, categories, products)
}

fun mockStoreWith(block: MockStore.() -> Unit): Store = MockStore()
    .apply(block)
    .run {
        Store(id, name, description, categories, products)
    }
