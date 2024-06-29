package br.com.rodrigogurgel.catalogservice.application.usecase.category

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface DeleteCategoryUseCase {
    fun execute(storeId: Id, categoryId: Id)
}
