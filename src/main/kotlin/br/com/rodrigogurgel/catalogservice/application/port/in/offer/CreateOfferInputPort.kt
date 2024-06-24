package br.com.rodrigogurgel.catalogservice.application.port.`in`.offer

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CreateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CreateOfferInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
) : CreateOfferUseCase {
    override fun execute(storeId: Id, categoryId: Id, offer: Offer) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryDatastoreOutputPort.exists(storeId, categoryId)) throw CategoryNotFoundException(categoryId)

        offerDatastoreOutputPort.create(storeId, categoryId, offer)
    }
}
