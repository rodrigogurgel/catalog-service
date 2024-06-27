package br.com.rodrigogurgel.catalogservice.domain.exception

class QuantityMaxPermittedZeroException(maxPermitted: Int) :
    IllegalArgumentException(
        """
The maximum permitted must be greater than zero. 
Actual value is [$maxPermitted] 
        """.trimIndent()
    )
