package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.ProductAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CreateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CreateProductInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val productOutputPort: ProductOutputPort,
) : CreateProductUseCase {
    override fun execute(storeId: Id, product: Product) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (productOutputPort.exists(product.id)) throw ProductAlreadyExistsException(product.id)

        productOutputPort.create(storeId, product)
    }
}
