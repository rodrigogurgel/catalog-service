package br.com.rodrigogurgel.catalogservice.application.port.`in`.product

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DeleteProductInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : DeleteProductUseCase {
    override fun execute(storeId: Id, productId: Id) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        productDatastoreOutputPort.delete(storeId, productId)
    }
}
