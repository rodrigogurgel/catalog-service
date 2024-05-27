package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Product
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(
    private val productDatastore: ProductDatastoreOutputPort,
//    private val idempotencyOutputPort: IdempotencyOutputPort,
) : ProductInputPort {

    override suspend fun create(product: Product): Result<Unit, Throwable> = productDatastore.create(product)

    override suspend fun update(product: Product): Result<Unit, Throwable> = productDatastore.update(product)

    override suspend fun delete(storeId: UUID, productId: UUID): Result<Unit, Throwable> =
        productDatastore.delete(storeId, productId)

    override suspend fun patch(product: Product): Result<Unit, Throwable> = productDatastore.patch(product)

    override suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable> =
        productDatastore.find(storeId, productIds)
}
