package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface GetOfferUseCase {
    fun execute(storeId: Id, offerId: Id): Offer
}
