package br.com.rodrigogurgel.catalogservice.domain.exception

import java.math.BigDecimal

class PriceNegativeException(price: BigDecimal) :
    IllegalArgumentException("The price must be positive. actual value is [$price]")
