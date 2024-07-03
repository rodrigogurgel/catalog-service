package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CountProductsUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CountProductsInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val productOutputPort: ProductOutputPort,
) : CountProductsUseCase {

    override fun execute(storeId: Id, beginsWith: String?): Int {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        validateBeginsWith(beginsWith)

        return productOutputPort.countProducts(
            storeId,
            beginsWith
        )
    }
}
