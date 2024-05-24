package br.com.rodrigogurgel.catalog.application.service

import br.com.rodrigogurgel.catalog.domain.Customization
import br.com.rodrigogurgel.catalog.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalog.application.port.out.datastore.CustomizationDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class CustomizationService(
    private val customizationDatastoreOutputPort: CustomizationDatastoreOutputPort,
) : CustomizationInputPort {

    override fun create(customization: Customization): Result<Unit, Throwable> = runCatching {
        customizationDatastoreOutputPort.create(customization)
    }.onFailure {
        throw it
    }

    override fun update(customization: Customization): Result<Unit, Throwable> = runCatching {
        customizationDatastoreOutputPort.update(customization)
    }

    override fun delete(storeId: UUID, customizationId: UUID): Result<Unit, Throwable> = runCatching {
        customizationDatastoreOutputPort.delete(storeId, customizationId)
    }

    override fun patch(customization: Customization): Result<Unit, Throwable> = runCatching {
        customizationDatastoreOutputPort.patch(customization)
    }

    override fun searchByReferenceBeginsWith(storeId: UUID, reference: String): Result<List<Customization>, Throwable> = runCatching {
        customizationDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)
    }
}