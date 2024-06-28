package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateCustomizationInputPort(
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase,
) : UpdateCustomizationUseCase {
    override fun execute(storeId: Id, offerId: Id, customization: Customization) {
        val offer = getOfferUseCase.execute(storeId, offerId)

        offer.updateCustomization(customization)

        updateOfferUseCase.execute(storeId, offer)
    }
}
