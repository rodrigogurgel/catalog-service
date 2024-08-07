package br.com.rodrigogurgel.catalogservice.application.port.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface OfferDatastoreOutputPort {
    fun create(storeId: Id, categoryId: Id, offer: Offer)
    fun findById(storeId: Id, offerId: Id): Offer?
    fun exists(offerId: Id): Boolean
    fun exists(storeId: Id, offerId: Id): Boolean
    fun update(storeId: Id, offer: Offer)
    fun delete(storeId: Id, offerId: Id)
    fun getOffers(storeId: Id, categoryId: Id, limit: Int, offset: Int, beginsWith: String?): List<Offer>
    fun countOffers(storeId: Id, categoryId: Id, beginsWith: String?): Int
}
