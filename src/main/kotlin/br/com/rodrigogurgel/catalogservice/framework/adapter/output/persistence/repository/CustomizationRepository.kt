package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data.CustomizationData
import java.util.UUID

interface CustomizationRepository {
    fun createBatch(customizations: List<CustomizationData>)
    fun getCustomizationsByOfferIds(storeId: UUID, offerIds: List<UUID>): List<CustomizationData>
    fun updateBatch(customizations: List<CustomizationData>)
    fun deleteIfNotIn(storeId: UUID, offerId: UUID, customizationIds: List<UUID>)
}
