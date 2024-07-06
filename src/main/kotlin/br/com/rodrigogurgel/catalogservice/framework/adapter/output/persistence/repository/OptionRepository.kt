package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.OptionData
import java.util.UUID

interface OptionRepository {
    fun createBatch(options: List<OptionData>)
    fun getOptionsByOfferIds(storeId: UUID, offerIds: List<UUID>): List<OptionData>
    fun updateBatch(options: List<OptionData>)
    fun deleteIfNotIn(storeId: UUID, offerId: UUID, optionIds: List<UUID>)
}
