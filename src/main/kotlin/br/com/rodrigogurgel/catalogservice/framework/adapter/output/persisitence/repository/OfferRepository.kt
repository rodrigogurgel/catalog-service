package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.OfferData
import java.util.UUID

interface OfferRepository {
    fun create(offerData: OfferData)
    fun findById(storeId: UUID, offerId: UUID): OfferData?
    fun exists(offerId: UUID): Boolean
    fun exists(storeId: UUID, offerId: UUID): Boolean
    fun update(offerData: OfferData)
    fun delete(storeId: UUID, offerId: UUID)
    fun getOffersByProductIdIncludingChildren(productId: UUID): List<UUID>
}