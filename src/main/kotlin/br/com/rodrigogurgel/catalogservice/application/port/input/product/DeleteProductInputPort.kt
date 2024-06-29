package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DeleteProductInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,

    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : DeleteProductUseCase {
    override fun execute(storeId: Id, productId: Id) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        productDatastoreOutputPort.delete(storeId, productId)
    }
}
