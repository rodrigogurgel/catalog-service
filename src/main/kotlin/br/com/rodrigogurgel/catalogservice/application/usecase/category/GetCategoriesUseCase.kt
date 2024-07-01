package br.com.rodrigogurgel.catalogservice.application.usecase.category

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface GetCategoriesUseCase {
    fun execute(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Category>
}
