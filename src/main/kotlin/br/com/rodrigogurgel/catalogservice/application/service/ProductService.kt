package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.domain.Product
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ProductDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.coroutines.runSuspendCatching
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productDatastore: ProductDatastoreOutputPort,
) : ProductInputPort {

    override suspend fun create(product: Product): Result<Unit, Throwable> = runSuspendCatching {
        productDatastore.create(product)
    }.onFailure {
        throw it
    }

    override suspend fun update(product: Product): Result<Unit, Throwable> = runSuspendCatching {
        productDatastore.update(product)
    }.onFailure {
        throw it
    }

    override suspend fun delete(storeId: UUID, productId: UUID): Result<Unit, Throwable> = runSuspendCatching {
        productDatastore.delete(storeId, productId)
    }.onFailure {
        throw it
    }


    override suspend fun patch(product: Product): Result<Unit, Throwable> = runSuspendCatching {
        productDatastore.patch(product)
    }.onFailure {
        throw it
    }

    override suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable> =
        runSuspendCatching {
            productDatastore.find(storeId, productIds)
        }.onFailure {
            throw it
        }
}