package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class GetCategoryInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val categoryOutputPort: CategoryOutputPort,
) : GetCategoryUseCase {
    override fun execute(storeId: Id, categoryId: Id): Category {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        return categoryOutputPort.findById(storeId, categoryId) ?: throw CategoryNotFoundException(
            storeId,
            categoryId
        )
    }
}
