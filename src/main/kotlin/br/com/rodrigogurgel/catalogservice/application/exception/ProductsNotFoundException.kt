package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

data class ProductsNotFoundException(private val productIds: List<Id>) :
    IllegalStateException("${productIds.map { it.value }}")
