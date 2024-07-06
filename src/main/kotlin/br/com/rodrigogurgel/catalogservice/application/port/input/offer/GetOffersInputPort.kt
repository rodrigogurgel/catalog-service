package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.normalizeLimit
import br.com.rodrigogurgel.catalogservice.application.port.input.normalizeOffset
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOffersUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class GetOffersInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    private val offersDatastoreOutputPort: OfferDatastoreOutputPort,
) : GetOffersUseCase {
    override fun execute(storeId: Id, categoryId: Id, limit: Int, offset: Int, beginsWith: String?): List<Offer> {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryDatastoreOutputPort.exists(storeId, categoryId)) {
            throw CategoryNotFoundException(
                storeId,
                categoryId
            )
        }

        validateBeginsWith(beginsWith)

        return offersDatastoreOutputPort.getOffers(
            storeId,
            categoryId,
            normalizeLimit(limit),
            normalizeOffset(offset),
            beginsWith
        )
    }
}
