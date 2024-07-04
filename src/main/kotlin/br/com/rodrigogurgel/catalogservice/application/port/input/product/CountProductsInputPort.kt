package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CountProductsUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CountProductsInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : CountProductsUseCase {

    override fun execute(storeId: Id, beginsWith: String?): Int {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        validateBeginsWith(beginsWith)

        return productDatastoreOutputPort.countProducts(
            storeId,
            beginsWith
        )
    }
}
