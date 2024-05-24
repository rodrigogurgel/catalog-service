package br.com.rodrigogurgel.catalog.application.port.out.datastore

import br.com.rodrigogurgel.catalog.domain.Customization
import java.util.UUID

interface CustomizationDatastoreOutputPort {
    fun create(customization: Customization)
    fun update(customization: Customization)
    fun delete(storeId: UUID, customizationId: UUID)
    fun patch(customization: Customization)
    fun searchByReferenceBeginsWith(storeId: UUID, reference: String): List<Customization>
}