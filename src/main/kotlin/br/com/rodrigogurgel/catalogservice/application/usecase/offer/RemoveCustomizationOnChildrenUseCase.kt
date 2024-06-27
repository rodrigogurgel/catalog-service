package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface RemoveCustomizationOnChildrenUseCase {
    fun execute(offerId: Id, optionId: Id, customizationId: Id)
}
