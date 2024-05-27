package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Item
import com.github.michaelbull.result.Result
import java.util.UUID

interface ItemInputPort {
    suspend fun create(idempotencyId: UUID, correlationId: UUID, item: Item): Result<Unit, Throwable>
    suspend fun update(idempotencyId: UUID, correlationId: UUID, item: Item): Result<Unit, Throwable>
    suspend fun delete(idempotencyId: UUID, correlationId: UUID, item: Item): Result<Unit, Throwable>
    suspend fun patch(idempotencyId: UUID, correlationId: UUID, item: Item): Result<Unit, Throwable>
    suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable>
}
