package br.com.rodrigogurgel.catalogservice.fixture.mock

import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.vo.Customization
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.randomString

class MockCustomization(
    var name: Name = Name(randomString(30)),
    var description: Description? = Description(randomString(100)),
    var quantity: Quantity = Quantity(0, 1),
    var status: Status = Status.AVAILABLE,
    var options: MutableList<Option> = mutableListOf(mockOption()),
)

fun mockCustomization(): Customization = MockCustomization().run {
    Customization(name, description, quantity, status, options)
}

fun mockCustomizationWith(block: MockCustomization.() -> Unit): Customization = MockCustomization()
    .apply(block)
    .run { Customization(name, description, quantity, status, options) }
