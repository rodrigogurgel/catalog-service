package br.com.rodrigogurgel.catalogservice.domain.vo

data class Customization(
    val name: Name,
    val description: Description?,
    val quantity: Quantity,
    val status: Status,
    val options: MutableList<Option>,
) {
    init {
        require((options.isNotEmpty()) && (options.size >= quantity.maxPermitted))
    }

    fun minimalPrice(): Price {
        return options.map { it.minimalPrice() }
            .sortedBy { it.normalizedValue() }
            .take(quantity.minPermitted)
            .sumOf { it.normalizedValue() }
            .let { Price(it) }
    }
}
