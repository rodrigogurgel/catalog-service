package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class ProductAlreadyExistsException(productId: Id) :
    IllegalArgumentException("Product with the id $productId already exists")
