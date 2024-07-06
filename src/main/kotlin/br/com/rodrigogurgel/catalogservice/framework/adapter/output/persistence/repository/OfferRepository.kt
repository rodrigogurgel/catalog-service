package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.data.OfferData
import java.util.UUID

interface OfferRepository {
    fun create(offerData: OfferData)
    fun findById(storeId: UUID, offerId: UUID): OfferData?
    fun exists(offerId: UUID): Boolean
    fun exists(storeId: UUID, offerId: UUID): Boolean
    fun update(offerData: OfferData)
    fun delete(storeId: UUID, offerId: UUID)
    fun countOffers(storeId: UUID, categoryId: UUID, beginsWith: String?): Int
    fun getOffers(storeId: UUID, categoryId: UUID, limit: Int, offset: Int, beginsWith: String?): List<OfferData>
}
