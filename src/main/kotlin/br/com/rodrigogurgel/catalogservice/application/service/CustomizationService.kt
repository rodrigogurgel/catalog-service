package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.IdempotencyInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CustomizationDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Customization
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomizationService(
    private val customizationDatastoreOutputPort: CustomizationDatastoreOutputPort,
    private val idempotencyInputPort: IdempotencyInputPort,
) : CustomizationInputPort {
    override suspend fun create(
        idempotencyId: UUID,
        correlationId: UUID,
        customization: Customization,
    ): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(idempotencyId, correlationId, customization.storeId!!) {
            customizationDatastoreOutputPort.create(customization)
        }

    override suspend fun update(
        idempotencyId: UUID,
        correlationId: UUID,
        customization: Customization,
    ): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            customization.storeId!!
        ) { customizationDatastoreOutputPort.update(customization) }

    override suspend fun delete(
        idempotencyId: UUID,
        correlationId: UUID,
        customization: Customization,
    ): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            customization.storeId!!
        ) { customizationDatastoreOutputPort.delete(customization.storeId, customization.customizationId!!) }

    override suspend fun patch(
        idempotencyId: UUID,
        correlationId: UUID,
        customization: Customization,
    ): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            customization.storeId!!
        ) { customizationDatastoreOutputPort.patch(customization) }

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Customization>, Throwable> =
        customizationDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)
}
