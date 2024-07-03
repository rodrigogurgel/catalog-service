package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.UpdateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateCategoryInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val categoryOutputPort: CategoryOutputPort,
) : UpdateCategoryUseCase {
    override fun execute(storeId: Id, category: Category) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryOutputPort.exists(storeId, category.id)) {
            throw CategoryNotFoundException(
                storeId,
                category.id
            )
        }

        categoryOutputPort.update(storeId, category)
    }
}
