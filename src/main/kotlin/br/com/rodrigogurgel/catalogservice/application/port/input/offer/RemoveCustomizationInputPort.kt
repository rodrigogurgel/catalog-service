package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class RemoveCustomizationInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val offerOutputPort: OfferOutputPort,
) : RemoveCustomizationUseCase {
    override fun execute(storeId: Id, offerId: Id, customizationId: Id) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        val offer = offerOutputPort
            .findById(storeId, offerId) ?: throw OfferNotFoundException(storeId, offerId)

        offer.removeCustomization(customizationId)

        offerOutputPort.update(storeId, offer)
    }
}
