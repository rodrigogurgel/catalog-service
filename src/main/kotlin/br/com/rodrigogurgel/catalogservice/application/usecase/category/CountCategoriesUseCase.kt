package br.com.rodrigogurgel.catalogservice.application.usecase.category

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface CountCategoriesUseCase {
    fun execute(storeId: Id, beginsWith: String?): Int
}
