package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.UpdateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateCategoryInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : UpdateCategoryUseCase {
    override fun execute(storeId: Id, category: Category) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryDatastoreOutputPort.exists(storeId, category.id)) {
            throw CategoryNotFoundException(
                storeId,
                category.id
            )
        }

        categoryDatastoreOutputPort.update(storeId, category)
    }
}
