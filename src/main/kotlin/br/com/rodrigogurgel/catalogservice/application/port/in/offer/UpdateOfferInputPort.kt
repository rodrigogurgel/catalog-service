package br.com.rodrigogurgel.catalogservice.application.port.`in`.offer

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateOfferInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
) : UpdateOfferUseCase {
    override fun execute(storeId: Id, categoryId: Id, offer: Offer) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryDatastoreOutputPort.exists(storeId, categoryId)) throw CategoryNotFoundException(categoryId)
        if (!offerDatastoreOutputPort.exists(storeId, categoryId, offer.id)) throw OfferNotFoundException(offer.id)

        offerDatastoreOutputPort.update(storeId, categoryId, offer)
    }
}
