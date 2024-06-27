package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface RemoveCustomizationUseCase {
    fun execute(offerId: Id, customizationId: Id)
}
