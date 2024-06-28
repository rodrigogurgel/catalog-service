package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class ProductNotFoundException private constructor(override val message: String) : IllegalStateException(message) {
    constructor(storeId: Id, productId: Id) : this("Product with id ${productId.value} and Store id $storeId not found")
}
