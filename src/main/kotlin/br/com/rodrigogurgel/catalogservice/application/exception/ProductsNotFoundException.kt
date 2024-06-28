package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class ProductsNotFoundException(productIds: List<Id>) :
    IllegalStateException("${productIds.map { it.value }}")
