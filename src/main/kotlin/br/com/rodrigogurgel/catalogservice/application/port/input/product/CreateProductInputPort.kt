package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.ProductAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CreateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CreateProductInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : CreateProductUseCase {
    override fun execute(storeId: Id, product: Product) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (productDatastoreOutputPort.exists(product.id)) throw ProductAlreadyExistsException(product.id)

        productDatastoreOutputPort.create(storeId, product)
    }
}
