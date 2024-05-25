package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.domain.Customization
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CustomizationDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.coroutines.runSuspendCatching
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class CustomizationService(
    private val customizationDatastoreOutputPort: CustomizationDatastoreOutputPort,
) : CustomizationInputPort {
    override suspend fun create(customization: Customization): Result<Unit, Throwable> = runSuspendCatching {
        customizationDatastoreOutputPort.create(customization)
    }.onFailure {
        throw it
    }

    override suspend fun update(customization: Customization): Result<Unit, Throwable> = runSuspendCatching {
        customizationDatastoreOutputPort.update(customization)
    }

    override suspend fun delete(storeId: UUID, customizationId: UUID): Result<Unit, Throwable> = runSuspendCatching {
        customizationDatastoreOutputPort.delete(storeId, customizationId)
    }

    override suspend fun patch(customization: Customization): Result<Unit, Throwable> = runSuspendCatching {
        customizationDatastoreOutputPort.patch(customization)
    }

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Customization>, Throwable> = runSuspendCatching {
        customizationDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)
    }
}