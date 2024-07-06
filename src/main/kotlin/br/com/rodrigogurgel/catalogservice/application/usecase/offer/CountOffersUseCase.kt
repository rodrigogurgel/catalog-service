package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface CountOffersUseCase {
    fun execute(storeId: Id, categoryId: Id, beginsWith: String?): Int
}
