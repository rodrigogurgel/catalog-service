package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class RemoveOptionOnChildrenInputPort(
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase,
) : RemoveOptionOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, customizationId: Id, optionId: Id) {
        val offer = getOfferUseCase.execute(storeId, offerId)
        val customization =
            offer.findCustomizationInChildrenById(customizationId) ?: throw CustomizationNotFoundException(
                customizationId
            )

        customization.removeOption(optionId)

        updateOfferUseCase.execute(storeId, offer)
    }
}
