package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Idempotency
import com.github.michaelbull.result.Result
import java.util.UUID

interface IdempotencyOutputPort {
    suspend fun create(idempotency: Idempotency): Result<Unit, Throwable>
    suspend fun patch(idempotency: Idempotency): Result<Unit, Throwable>
    suspend fun search(idempotencyId: UUID): Result<Idempotency?, Throwable>
}
