package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Category
import com.github.michaelbull.result.Result
import java.util.UUID

interface CategoryInputPort {
    suspend fun create(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable>
    suspend fun update(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable>
    suspend fun delete(
        idempotencyId: UUID,
        correlationId: UUID,
        category: Category,
    ): Result<Unit, Throwable>

    suspend fun patch(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable>
}
