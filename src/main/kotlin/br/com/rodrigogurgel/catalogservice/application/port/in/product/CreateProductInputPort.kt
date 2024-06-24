package br.com.rodrigogurgel.catalogservice.application.port.`in`.product

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CreateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CreateProductInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : CreateProductUseCase {
    override fun execute(storeId: Id, product: Product) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        productDatastoreOutputPort.create(storeId, product)
    }
}
