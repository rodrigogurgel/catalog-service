package br.com.rodrigogurgel.catalogservice.application.usecase.product

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface DeleteProductUseCase {
    fun execute(storeId: Id, productId: Id)
}
