package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class RemoveOptionOnChildrenInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val offerOutputPort: OfferOutputPort,
) : RemoveOptionOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, customizationId: Id, optionId: Id) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        val offer = offerOutputPort
            .findById(storeId, offerId) ?: throw OfferNotFoundException(storeId, offerId)

        val customization =
            offer.findCustomizationInChildrenById(customizationId) ?: throw CustomizationNotFoundException(
                customizationId
            )

        customization.removeOption(optionId)

        offerOutputPort.update(storeId, offer)
    }
}
