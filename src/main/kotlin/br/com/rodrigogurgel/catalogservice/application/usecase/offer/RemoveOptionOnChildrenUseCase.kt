package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface RemoveOptionOnChildrenUseCase {
    fun execute(storeId: Id, offerId: Id, customizationId: Id, optionId: Id)
}
