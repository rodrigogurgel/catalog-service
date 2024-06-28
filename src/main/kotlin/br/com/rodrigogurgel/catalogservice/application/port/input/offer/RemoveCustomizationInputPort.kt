package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class RemoveCustomizationInputPort(
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase,
) : RemoveCustomizationUseCase {
    override fun execute(storeId: Id, offerId: Id, customizationId: Id) {
        val offer = getOfferUseCase.execute(storeId, offerId)

        offer.removeCustomization(customizationId)

        updateOfferUseCase.execute(storeId, offer)
    }
}
