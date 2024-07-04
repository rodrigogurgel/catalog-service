package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.OptionData
import java.util.UUID

interface OptionRepository {
    fun createBatch(options: List<OptionData>)
    fun getOptionsByOfferId(storeId: UUID, offerId: UUID): List<OptionData>
    fun updateBatch(options: List<OptionData>)
    fun deleteIfNotIn(storeId: UUID, offerId: UUID, optionIds: List<UUID>)
}
