package br.com.rodrigogurgel.catalogservice.application.port.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface OfferDatastoreOutputPort {
    fun create(storeId: Id, categoryId: Id, offer: Offer)
    fun findById(storeId: Id, categoryId: Id, offerId: Id): Offer?
    fun exists(storeId: Id, categoryId: Id, offerId: Id): Boolean
    fun update(storeId: Id, categoryId: Id, offer: Offer)
    fun delete(storeId: Id, categoryId: Id, offerId: Id)
}
