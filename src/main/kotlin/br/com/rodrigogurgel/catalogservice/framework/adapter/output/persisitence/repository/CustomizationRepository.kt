package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.CustomizationData
import java.util.UUID

interface CustomizationRepository {
    fun createBatch(customizations: List<CustomizationData>)
    fun getCustomizationsByOfferId(storeId: UUID, offerId: UUID): List<CustomizationData>
    fun updateBatch(customizations: List<CustomizationData>)
    fun deleteIfNotIn(storeId: UUID, offerId: UUID, customizationIds: List<UUID>)
}
