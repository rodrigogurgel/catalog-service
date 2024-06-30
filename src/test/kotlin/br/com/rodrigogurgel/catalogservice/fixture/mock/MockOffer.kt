package br.com.rodrigogurgel.catalogservice.fixture.mock

import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import java.math.BigDecimal
import kotlin.random.Random

data class MockOffer(
    var id: Id = Id(),
    var name: Name = Name(randomString(30)),
    var product: Product = mockProduct(),
    var price: Price = Price(BigDecimal.valueOf(Random.nextDouble(0.1, 100.0))),
    var status: Status = Status.AVAILABLE,
    var customizations: List<Customization> = mutableListOf(),
)

fun mockOffer(): Offer = MockOffer().run {
    Offer(this.id, this.name, this.product, this.price, this.status, this.customizations)
}

fun mockOfferWith(block: MockOffer.() -> Unit): Offer = MockOffer()
    .apply(block)
    .run {
        Offer(this.id, this.name, this.product, this.price, this.status, this.customizations)
    }
