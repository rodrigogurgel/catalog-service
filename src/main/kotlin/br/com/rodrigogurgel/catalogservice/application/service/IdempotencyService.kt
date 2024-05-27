package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.idempotency.IdempotencyAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.IdempotencyInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.IdempotencyOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Idempotency
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class IdempotencyService(
    private val idempotencyOutputPort: IdempotencyOutputPort,
) : IdempotencyInputPort {
    override suspend fun runWithIdempotency(
        idempotencyId: UUID,
        correlationId: UUID,
        storeId: UUID,
        block: suspend () -> Result<Unit, Throwable>,
    ): Result<Unit, Throwable> =
        idempotencyOutputPort.create(Idempotency.created(idempotencyId, correlationId, storeId))
            .andThen { block() }
            .onSuccess {
                idempotencyOutputPort.patch(Idempotency.success(idempotencyId))
            }
            .onFailure { error ->
                when (error) {
                    !is IdempotencyAlreadyExistsException -> idempotencyOutputPort.patch(
                        Idempotency.failure(
                            idempotencyId,
                        )
                    ).andThen {
                        Err(error)
                    }
                }
            }

    override suspend fun patch(idempotency: Idempotency): Result<Unit, Throwable> =
        idempotencyOutputPort.patch(idempotency)
}