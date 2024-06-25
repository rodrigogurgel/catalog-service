package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status

data class Option(
    val id: Id,
    val product: Product,
    val price: Price,
    val quantity: Quantity,
    val status: Status,
    val customizations: List<Customization>,
) {
    fun minimalPrice(): Price {
        val minPermittedOrOne = if (quantity.minPermitted == 0) 1 else quantity.minPermitted

        return Price(
            (price.normalizedValue() * minPermittedOrOne.toBigDecimal()) + customizations.sumOf {
                it.minimalPrice().normalizedValue()
            }
        )
    }
}
