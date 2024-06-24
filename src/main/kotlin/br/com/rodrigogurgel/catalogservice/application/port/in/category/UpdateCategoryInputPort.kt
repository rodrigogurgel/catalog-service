package br.com.rodrigogurgel.catalogservice.application.port.`in`.category

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.UpdateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateCategoryInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : UpdateCategoryUseCase {
    override fun execute(storeId: Id, category: Category) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryDatastoreOutputPort.exists(storeId, category.id)) throw CategoryNotFoundException(category.id)

        categoryDatastoreOutputPort.update(storeId, category)
    }
}
