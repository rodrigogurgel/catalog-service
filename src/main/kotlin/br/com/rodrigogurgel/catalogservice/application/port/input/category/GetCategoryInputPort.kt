package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class GetCategoryInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : GetCategoryUseCase {
    override fun execute(storeId: Id, categoryId: Id): Category {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        return categoryDatastoreOutputPort.findById(storeId, categoryId) ?: throw CategoryNotFoundException(
            storeId,
            categoryId
        )
    }
}
