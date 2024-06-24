package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface CreateOfferUseCase {
    fun execute(storeId: Id, categoryId: Id, offer: Offer)
}
