package br.com.rodrigogurgel.catalogservice.fixture.mock

import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Customization
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.math.BigDecimal
import kotlin.random.Random

class MockOption(
    var id: Id = Id(),
    var product: Product = mockProduct(),
    var price: Price = Price(BigDecimal.valueOf(Random.nextDouble(0.0, 100.0))),
    var quantity: Quantity = Quantity(0, 1),
    var status: Status = Status.AVAILABLE,
    var customizations: MutableList<Customization> = mutableListOf(),
)

fun mockOption() = MockOption()
    .run { Option(id, product, price, quantity, status, customizations) }

fun mockOptionWith(block: MockOption.() -> Unit): Option =
    MockOption()
        .apply(block)
        .run { Option(id, product, price, quantity, status, customizations) }
