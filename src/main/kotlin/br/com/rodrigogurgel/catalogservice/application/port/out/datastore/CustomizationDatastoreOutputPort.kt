package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Customization
import java.util.UUID

interface CustomizationDatastoreOutputPort {
    suspend fun create(customization: Customization)
    suspend fun update(customization: Customization)
    suspend fun delete(storeId: UUID, customizationId: UUID)
    suspend fun patch(customization: Customization)
    suspend fun searchByReferenceBeginsWith(storeId: UUID, reference: String): List<Customization>
}