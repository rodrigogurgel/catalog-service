package br.com.rodrigogurgel.catalogservice.application.usecase.category

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface CreateCategoryUseCase {
    fun execute(storeId: Id, category: Category)
}
