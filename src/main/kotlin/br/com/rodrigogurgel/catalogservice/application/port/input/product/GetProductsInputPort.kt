package br.com.rodrigogurgel.catalogservice.application.port.input.product

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.normalizeLimit
import br.com.rodrigogurgel.catalogservice.application.port.input.normalizeOffset
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductsUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class GetProductsInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : GetProductsUseCase {

    override fun execute(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Product> {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        validateBeginsWith(beginsWith)

        return productDatastoreOutputPort.getProducts(
            storeId,
            normalizeLimit(limit),
            normalizeOffset(offset),
            beginsWith
        )
    }
}
