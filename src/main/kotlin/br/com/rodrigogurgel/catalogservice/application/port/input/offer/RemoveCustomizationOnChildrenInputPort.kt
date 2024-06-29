package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class RemoveCustomizationOnChildrenInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
) : RemoveCustomizationOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, optionId: Id, customizationId: Id) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        val offer = offerDatastoreOutputPort
            .findById(storeId, offerId) ?: throw OfferNotFoundException(storeId, offerId)

        val option = offer.findOptionInChildrenById(optionId) ?: throw OptionNotFoundException(offerId)

        option.removeCustomization(customizationId)

        offerDatastoreOutputPort.update(storeId, offer)
    }
}
