package br.com.rodrigogurgel.catalogservice.application.usecase.product

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface GetProductUseCase {
    fun execute(storeId: Id, productId: Id): Product
}
