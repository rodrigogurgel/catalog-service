package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class RemoveOptionOnChildrenInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
) : RemoveOptionOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, customizationId: Id, optionId: Id) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        val offer = offerDatastoreOutputPort
            .findById(storeId, offerId) ?: throw OfferNotFoundException(storeId, offerId)

        val customization =
            offer.findCustomizationInChildrenById(customizationId) ?: throw CustomizationNotFoundException(
                customizationId
            )

        customization.removeOption(optionId)

        offerDatastoreOutputPort.update(storeId, offer)
    }
}
