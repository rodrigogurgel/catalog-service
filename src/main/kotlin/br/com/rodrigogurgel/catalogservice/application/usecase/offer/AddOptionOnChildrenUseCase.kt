package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface AddOptionOnChildrenUseCase {
    fun execute(storeId: Id, offerId: Id, customizationId: Id, option: Option)
}
