package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.DeleteCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DeleteCategoryInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val categoryOutputPort: CategoryOutputPort,
) : DeleteCategoryUseCase {
    override fun execute(storeId: Id, categoryId: Id) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        categoryOutputPort.delete(storeId, categoryId)
    }
}
