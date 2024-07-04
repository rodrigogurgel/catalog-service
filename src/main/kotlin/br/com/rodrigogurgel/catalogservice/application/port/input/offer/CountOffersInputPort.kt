package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.validateBeginsWith
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CountOffersUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CountOffersInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    private val offersDatastoreOutputPort: OfferDatastoreOutputPort,
) : CountOffersUseCase {
    override fun execute(storeId: Id, categoryId: Id, beginsWith: String?): Int {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryDatastoreOutputPort.exists(storeId, categoryId)) {
            throw CategoryNotFoundException(
                storeId,
                categoryId
            )
        }

        validateBeginsWith(beginsWith)

        return offersDatastoreOutputPort.countOffers(storeId, categoryId, beginsWith)
    }
}
