package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateCustomizationOnChildrenInputPort(
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase,
) : UpdateCustomizationOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, optionId: Id, customization: Customization) {
        val offer = getOfferUseCase.execute(storeId, offerId)
        val option = offer.findOptionInChildrenById(optionId) ?: throw OptionNotFoundException(optionId)

        option.updateCustomization(customization)

        updateOfferUseCase.execute(storeId, offer)
    }
}
