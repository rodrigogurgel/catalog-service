package br.com.rodrigogurgel.catalog.application.service

import br.com.rodrigogurgel.catalog.domain.Product
import br.com.rodrigogurgel.catalog.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalog.application.port.out.datastore.ProductDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productDatastore: ProductDatastoreOutputPort,
) : ProductInputPort {

    override fun create(product: Product): Result<Unit, Throwable> = runCatching {
        productDatastore.create(product)
    }.onFailure {
        throw it
    }

    override fun update(product: Product): Result<Unit, Throwable> = runCatching {
        productDatastore.update(product)
    }

    override fun delete(storeId: UUID, productId: UUID): Result<Unit, Throwable> = runCatching {
        productDatastore.delete(storeId, productId)
    }


    override fun patch(product: Product): Result<Unit, Throwable> = runCatching {
        productDatastore.patch(product)
    }

    override fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable> = runCatching {
        productDatastore.find(storeId, productIds)
    }
}