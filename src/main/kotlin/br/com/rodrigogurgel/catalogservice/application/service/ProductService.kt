package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.IdempotencyInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Product
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(
    private val productDatastore: ProductDatastoreOutputPort,
    private val idempotencyInputPort: IdempotencyInputPort,
) : ProductInputPort {

    override suspend fun create(idempotencyId: UUID, correlationId: UUID, product: Product): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            product.storeId!!
        ) { productDatastore.create(product) }

    override suspend fun update(idempotencyId: UUID, correlationId: UUID, product: Product): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            product.storeId!!
        ) { productDatastore.update(product) }

    override suspend fun delete(idempotencyId: UUID, correlationId: UUID, product: Product): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            product.storeId!!
        ) { productDatastore.delete(product.storeId.toString().toUUID(), product.productId.toString().toUUID()) }

    override suspend fun patch(idempotencyId: UUID, correlationId: UUID, product: Product): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            product.storeId!!
        ) { productDatastore.patch(product) }

    override suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable> =
        productDatastore.find(storeId, productIds)
}
