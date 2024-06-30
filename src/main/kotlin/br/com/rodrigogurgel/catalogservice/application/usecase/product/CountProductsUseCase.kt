package br.com.rodrigogurgel.catalogservice.application.usecase.product

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface CountProductsUseCase {
    fun execute(storeId: Id, limit: Int, offset: Int, beginsWith: String?): Int
}
