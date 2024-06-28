package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class RemoveCustomizationOnChildrenInputPort(
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase,
) : RemoveCustomizationOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, optionId: Id, customizationId: Id) {
        val offer = getOfferUseCase.execute(storeId, offerId)
        val option = offer.findOptionInChildrenById(optionId) ?: throw OptionNotFoundException(offerId)

        option.removeCustomization(customizationId)

        updateOfferUseCase.execute(storeId, offer)
    }
}
