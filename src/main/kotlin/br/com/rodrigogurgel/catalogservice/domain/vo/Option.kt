package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.entity.Product

data class Option(
    val product: Product,
    val price: Price,
    val quantity: Quantity,
    val status: Status,
    val customizations: MutableList<Customization>,
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
