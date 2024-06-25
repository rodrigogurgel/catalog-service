package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

data class ProductNotFoundException(private val productId: Id) :
    IllegalStateException("Product with id ${productId.id} not found")
