package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class AddCustomizationOnChildrenInputPort(
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase
) : AddCustomizationOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, optionId: Id, customization: Customization) {
        val offer = getOfferUseCase.execute(storeId, offerId)
        val option = offer.findOptionInChildrenById(optionId) ?: throw OptionNotFoundException(offerId)

        option.addCustomization(customization)

        updateOfferUseCase.execute(storeId, offer)
    }
}
