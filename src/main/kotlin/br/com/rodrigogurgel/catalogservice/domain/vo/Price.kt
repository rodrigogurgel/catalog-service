package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.exception.PriceNegativeException
import java.math.BigDecimal
import java.math.RoundingMode

class Price(value: BigDecimal) {
    val value: BigDecimal = value.setScale(2, RoundingMode.UP)

    init {
        if (value.setScale(2, RoundingMode.UP) < BigDecimal.ZERO) throw PriceNegativeException(value)
    }

    companion object {
        val ZERO = Price(BigDecimal.ZERO)
    }
}
