package br.com.rodrigogurgel.catalogservice.fixture.mock

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Image
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.randomString

data class MockProduct(
    var id: Id = Id(),
    var name: Name = Name(randomString(30)),
    var description: Description? = Description(randomString(100)),
    var image: Image? = Image("https://www.${randomString(5)}.com"),
)

fun mockProduct() = MockProduct().run {
    Product(id, name, description, image)
}

fun mockProductWith(block: MockProduct.() -> Unit) = MockProduct()
    .apply(block)
    .run { Product(id, name, description, image) }
