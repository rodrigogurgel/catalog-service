package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.normalizeLimit
import br.com.rodrigogurgel.catalogservice.application.port.input.normalizeOffset
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class GetCategoriesInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val categoryOutputPort: CategoryOutputPort,
) : GetCategoriesUseCase {
    override fun execute(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Category> {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        validateBeginsWith(beginsWith)

        return categoryOutputPort.getCategories(
            storeId,
            normalizeLimit(limit),
            normalizeOffset(offset),
            beginsWith
        )
    }
}
