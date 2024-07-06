package br.com.rodrigogurgel.catalogservice.application.usecase.offer

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface GetOffersUseCase {
    fun execute(storeId: Id, categoryId: Id, limit: Int, offset: Int, beginsWith: String?): List<Offer>
}
