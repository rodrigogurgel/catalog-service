package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.DeleteCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DeleteCategoryInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : DeleteCategoryUseCase {
    override fun execute(storeId: Id, categoryId: Id) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        categoryDatastoreOutputPort.delete(storeId, categoryId)
    }
}
