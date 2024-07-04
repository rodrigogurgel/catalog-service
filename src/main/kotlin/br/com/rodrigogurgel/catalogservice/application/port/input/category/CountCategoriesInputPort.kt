package br.com.rodrigogurgel.catalogservice.application.port.input.category

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CountCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CountCategoriesInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : CountCategoriesUseCase {
    override fun execute(storeId: Id, beginsWith: String?): Int {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        validateBeginsWith(beginsWith)

        return categoryDatastoreOutputPort.countCategories(
            storeId,
            beginsWith
        )
    }
}
