package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface UpdateOfferUseCase {
    fun execute(storeId: Id, offer: Offer)
}
