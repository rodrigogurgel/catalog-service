package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface UpdateCustomizationUseCase {
    fun execute(storeId: Id, offerId: Id, customization: Customization)
}
