package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Idempotency
import com.github.michaelbull.result.Result
import java.util.UUID

interface IdempotencyInputPort {
    suspend fun runWithIdempotency(
        idempotencyId: UUID,
        correlationId: UUID,
        storeId: UUID,
        block: suspend () -> Result<Unit, Throwable>,
    ): Result<Unit, Throwable>

    suspend fun patch(idempotency: Idempotency): Result<Unit, Throwable>
}
