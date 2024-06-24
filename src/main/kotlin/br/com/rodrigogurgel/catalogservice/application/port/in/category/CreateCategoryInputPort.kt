package br.com.rodrigogurgel.catalogservice.application.port.`in`.category

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CreateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CreateCategoryInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : CreateCategoryUseCase {
    override fun execute(storeId: Id, category: Category) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        categoryDatastoreOutputPort.create(storeId, category)
    }
}
