package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Customization
import com.github.michaelbull.result.Result
import java.util.UUID

interface CustomizationInputPort {
    suspend fun create(idempotencyId: UUID, correlationId: UUID, customization: Customization): Result<Unit, Throwable>
    suspend fun update(idempotencyId: UUID, correlationId: UUID, customization: Customization): Result<Unit, Throwable>
    suspend fun patch(idempotencyId: UUID, correlationId: UUID, customization: Customization): Result<Unit, Throwable>
    suspend fun delete(idempotencyId: UUID, correlationId: UUID, customization: Customization): Result<Unit, Throwable>
    suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Customization>, Throwable>
}
