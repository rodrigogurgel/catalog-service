package br.com.rodrigogurgel.catalogservice.application.port.`in`.category

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class GetCategoryInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : GetCategoryUseCase {
    override fun execute(storeId: Id, categoryId: Id): Category {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        return categoryDatastoreOutputPort.findById(storeId, categoryId) ?: throw CategoryNotFoundException(storeId)
    }
}
