package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface DeleteOfferUseCase {
    fun execute(storeId: Id, offerId: Id)
}
