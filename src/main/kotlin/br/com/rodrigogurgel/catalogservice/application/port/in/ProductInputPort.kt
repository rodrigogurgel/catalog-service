package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Product
import com.github.michaelbull.result.Result
import java.util.UUID

interface ProductInputPort {
    suspend fun create(idempotencyId: UUID, correlationId: UUID, product: Product): Result<Unit, Throwable>
    suspend fun update(idempotencyId: UUID, correlationId: UUID, product: Product): Result<Unit, Throwable>
    suspend fun delete(idempotencyId: UUID, correlationId: UUID, product: Product): Result<Unit, Throwable>
    suspend fun patch(idempotencyId: UUID, correlationId: UUID, product: Product): Result<Unit, Throwable>
    suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable>
}
