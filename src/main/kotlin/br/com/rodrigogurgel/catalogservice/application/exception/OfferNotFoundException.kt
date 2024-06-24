package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

data class OfferNotFoundException(private val storeId: Id) :
    RuntimeException("Store with id ${storeId.id} not found")
