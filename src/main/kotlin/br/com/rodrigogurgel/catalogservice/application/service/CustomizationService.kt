package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CustomizationDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Customization
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomizationService(
    private val customizationDatastoreOutputPort: CustomizationDatastoreOutputPort,
) : CustomizationInputPort {
    override suspend fun create(customization: Customization): Result<Unit, Throwable> =
        customizationDatastoreOutputPort.create(customization)

    override suspend fun update(customization: Customization): Result<Unit, Throwable> =
        customizationDatastoreOutputPort.update(customization)

    override suspend fun delete(storeId: UUID, customizationId: UUID): Result<Unit, Throwable> =
        customizationDatastoreOutputPort.delete(storeId, customizationId)

    override suspend fun patch(customization: Customization): Result<Unit, Throwable> =
        customizationDatastoreOutputPort.patch(customization)

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Customization>, Throwable> =
        customizationDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)
}
