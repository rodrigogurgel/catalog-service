package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.ProductNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.UpdateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateProductInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : UpdateProductUseCase {
    override fun execute(storeId: Id, product: Product) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!productDatastoreOutputPort.exists(storeId, product.id)) throw ProductNotFoundException(storeId, product.id)

        productDatastoreOutputPort.update(storeId, product)
    }
}
