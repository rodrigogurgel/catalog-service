package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.IdempotencyInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.OptionDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Option
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OptionService(
    private val optionDatastoreOutputPort: OptionDatastoreOutputPort,
    private val idempotencyInputPort: IdempotencyInputPort,
) : OptionInputPort {
    override suspend fun create(idempotencyId: UUID, correlationId: UUID, option: Option): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            option.storeId!!
        ) { optionDatastoreOutputPort.create(option) }

    override suspend fun update(idempotencyId: UUID, correlationId: UUID, option: Option): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            option.storeId!!
        ) { optionDatastoreOutputPort.update(option) }

    override suspend fun delete(idempotencyId: UUID, correlationId: UUID, option: Option): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            option.storeId!!
        ) { optionDatastoreOutputPort.delete(option.storeId.toString().toUUID(), option.optionId.toString().toUUID()) }

    override suspend fun patch(idempotencyId: UUID, correlationId: UUID, option: Option): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            option.storeId!!
        ) { optionDatastoreOutputPort.patch(option) }

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Option>, Throwable> = optionDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)
}
