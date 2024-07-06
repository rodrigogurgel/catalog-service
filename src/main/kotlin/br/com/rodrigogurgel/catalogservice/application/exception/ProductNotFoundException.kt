package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class ProductNotFoundException(storeId: Id, productId: Id) :
    IllegalStateException("Product with the ${productId.value} and Store with the $storeId not found")
