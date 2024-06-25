package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status

data class Customization(
    val id: Id,
    val name: Name,
    val description: Description?,
    val quantity: Quantity,
    val status: Status,
    val options: List<Option>,
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
