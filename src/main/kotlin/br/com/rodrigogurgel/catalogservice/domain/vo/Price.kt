package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.exception.PriceNegativeException
import java.math.BigDecimal
import java.math.RoundingMode

@JvmInline
value class Price(
    private val value: BigDecimal,
) {
    fun normalizedValue(): BigDecimal = this.value.setScale(2, RoundingMode.UP)

    init {
        if (value < BigDecimal.ZERO) throw PriceNegativeException(value)
    }

    companion object {
        val ZERO = Price(BigDecimal.ZERO)
    }
}
