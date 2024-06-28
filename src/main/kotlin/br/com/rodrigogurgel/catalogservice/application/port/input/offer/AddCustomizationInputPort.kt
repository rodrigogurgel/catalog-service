package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class AddCustomizationInputPort(
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase,
) : AddCustomizationUseCase {

    override fun execute(storeId: Id, offerId: Id, customization: Customization) {
        val offer = getOfferUseCase.execute(storeId, offerId)

        offer.addCustomization(customization)

        updateOfferUseCase.execute(storeId, offer)
    }
}
