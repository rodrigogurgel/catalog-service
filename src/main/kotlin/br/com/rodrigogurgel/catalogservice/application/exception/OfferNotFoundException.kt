package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

data class OfferNotFoundException(private val storeId: Id) :
    IllegalStateException("Store with id ${storeId.value} not found")
