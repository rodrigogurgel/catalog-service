package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.entity.Option

data class Customization(
    val name: Name,
    val description: Description?,
    val quantity: Quantity,
    val status: Status,
    val options: MutableList<Option>,
) {
    init {
        require(options.isNotEmpty()) {
            "The options should not be empty."
        }

        require(options.size >= quantity.maxPermitted) {
            "The options must have more than quantity maximum permitted options."
        }
    }

    fun minimalPrice(): Price {
        return options.map { it.minimalPrice() }
            .sortedBy { it.normalizedValue() }
            .take(quantity.minPermitted)
            .sumOf { it.normalizedValue() }
            .let { Price(it) }
    }
}
