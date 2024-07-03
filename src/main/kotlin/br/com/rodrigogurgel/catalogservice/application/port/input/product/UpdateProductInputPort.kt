package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.ProductNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.UpdateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateProductInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val productOutputPort: ProductOutputPort,
) : UpdateProductUseCase {
    override fun execute(storeId: Id, product: Product) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!productOutputPort.exists(storeId, product.id)) throw ProductNotFoundException(storeId, product.id)

        productOutputPort.update(storeId, product)
    }
}
