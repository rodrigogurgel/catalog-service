package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.normalizeLimit
import br.com.rodrigogurgel.catalogservice.application.port.input.normalizeOffset
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class GetCategoriesInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : GetCategoriesUseCase {
    override fun execute(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Category> {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        validateBeginsWith(beginsWith)

        return categoryDatastoreOutputPort.getCategories(
            storeId,
            normalizeLimit(limit),
            normalizeOffset(offset),
            beginsWith
        )
    }
}
