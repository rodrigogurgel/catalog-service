package br.com.rodrigogurgel.catalogservice.application.usecase.product

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface UpdateProductUseCase {
    fun execute(storeId: Id, product: Product)
}
