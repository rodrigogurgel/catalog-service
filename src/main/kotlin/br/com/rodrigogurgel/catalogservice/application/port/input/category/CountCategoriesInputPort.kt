package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CountCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CountCategoriesInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val categoryOutputPort: CategoryOutputPort,
) : CountCategoriesUseCase {
    override fun execute(storeId: Id, beginsWith: String?): Int {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        validateBeginsWith(beginsWith)

        return categoryOutputPort.countCategories(
            storeId,
            beginsWith
        )
    }
}
