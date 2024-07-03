package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CreateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CreateCategoryInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val categoryOutputPort: CategoryOutputPort,
) : CreateCategoryUseCase {
    override fun execute(storeId: Id, category: Category) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (categoryOutputPort.exists(category.id)) throw CategoryAlreadyExistsException(category.id)

        categoryOutputPort.create(storeId, category)
    }
}
