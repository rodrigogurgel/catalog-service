package br.com.rodrigogurgel.catalogservice.application.port.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface OfferOutputPort {
    fun create(storeId: Id, categoryId: Id, offer: Offer)
    fun findById(storeId: Id, offerId: Id): Offer?
    fun exists(offerId: Id): Boolean
    fun exists(storeId: Id, offerId: Id): Boolean
    fun update(storeId: Id, offer: Offer)
    fun delete(storeId: Id, offerId: Id)
    fun getOffersByProductIdIncludingChildren(productId: Id): List<Id>
}
