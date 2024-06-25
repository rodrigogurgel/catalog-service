package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Status

data class Offer(
    val id: Id,
    val product: Product,
    val price: Price,
    val status: Status,
    val customizations: List<Customization>,
) {
    init {
        check(minimalPrice().normalizedValue() > Price.ZERO.normalizedValue())
    }

    fun minimalPrice(): Price {
        return Price(price.normalizedValue() + customizations.sumOf { it.minimalPrice().normalizedValue() })
    }
}
