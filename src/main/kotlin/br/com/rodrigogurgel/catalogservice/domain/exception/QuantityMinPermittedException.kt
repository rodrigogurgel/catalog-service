package br.com.rodrigogurgel.catalogservice.domain.exception

class QuantityMinPermittedException(minPermitted: Int) :
    IllegalArgumentException(
        "The minimum permitted must be greater or equals to zero. actual minimum permitted is [$minPermitted]"
    )
