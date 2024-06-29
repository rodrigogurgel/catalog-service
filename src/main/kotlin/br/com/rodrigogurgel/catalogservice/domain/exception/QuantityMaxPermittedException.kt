package br.com.rodrigogurgel.catalogservice.domain.exception

class QuantityMaxPermittedException(minPermitted: Int, maxPermitted: Int) :
    IllegalArgumentException(
        """The maximum permitted need be greater or equals to minimum permitted.
Actual values is maximum permitted: [$maxPermitted], minimum permitted: [$minPermitted]
        """.trimIndent()
    )
