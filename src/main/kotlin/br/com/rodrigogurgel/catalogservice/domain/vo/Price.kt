package br.com.rodrigogurgel.catalogservice.domain.vo

import java.math.BigDecimal
import java.math.RoundingMode

data class Price(
    private var value: BigDecimal,
) {
    fun normalizedValue(): BigDecimal = this.value.setScale(2, RoundingMode.UP)

    init {
        require(value >= BigDecimal.ZERO) {
            "The price must be positive."
        }
    }

    companion object {
        val ZERO = Price(BigDecimal.ZERO)
    }
}
