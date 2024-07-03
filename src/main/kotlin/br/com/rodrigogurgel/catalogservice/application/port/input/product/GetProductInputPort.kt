package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.ProductNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class GetProductInputPort(
    private val storeOutputPort: StoreOutputPort,

    private val productOutputPort: ProductOutputPort,
) : GetProductUseCase {
    override fun execute(storeId: Id, productId: Id): Product {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        return productOutputPort.findById(storeId, productId) ?: throw ProductNotFoundException(
            storeId,
            productId
        )
    }
}
