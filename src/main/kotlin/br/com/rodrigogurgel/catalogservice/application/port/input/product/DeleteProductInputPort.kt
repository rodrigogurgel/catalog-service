package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.ProductIsInUseException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DeleteProductInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val productOutputPort: ProductOutputPort,
) : DeleteProductUseCase {
    override fun execute(storeId: Id, productId: Id) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        if (productOutputPort.productIsInUse(productId)) throw ProductIsInUseException(productId)

        productOutputPort.delete(storeId, productId)
    }
}
