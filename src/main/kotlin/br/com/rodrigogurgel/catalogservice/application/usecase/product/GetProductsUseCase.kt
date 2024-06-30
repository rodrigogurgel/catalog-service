package br.com.rodrigogurgel.catalogservice.application.usecase.product

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface GetProductsUseCase {
    fun execute(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Product>
}
