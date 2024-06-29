package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.exception.QuantityMaxPermittedException
import br.com.rodrigogurgel.catalogservice.domain.exception.QuantityMaxPermittedZeroException
import br.com.rodrigogurgel.catalogservice.domain.exception.QuantityMinPermittedException

data class Quantity(
    val minPermitted: Int,
    val maxPermitted: Int,
) {
    init {
        if (minPermitted < 0) throw QuantityMinPermittedException(minPermitted)
        if (maxPermitted <= 0) throw QuantityMaxPermittedZeroException(maxPermitted)
        if (maxPermitted < minPermitted) throw QuantityMaxPermittedException(minPermitted, maxPermitted)
    }
}
