package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateOptionOnChildrenInputPort(
    private val getOfferUseCase: GetOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase,
) : UpdateOptionOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, customizationId: Id, option: Option) {
        val offer = getOfferUseCase.execute(storeId, offerId)
        val customization =
            offer.findCustomizationInChildrenById(customizationId) ?: throw CustomizationNotFoundException(
                customizationId
            )

        customization.updateOption(option)

        updateOfferUseCase.execute(storeId, offer)
    }
}
