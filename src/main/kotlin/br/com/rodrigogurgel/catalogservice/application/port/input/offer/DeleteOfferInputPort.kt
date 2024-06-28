package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.DeleteOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DeleteOfferInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
) : DeleteOfferUseCase {
    override fun execute(storeId: Id, offerId: Id) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        return offerDatastoreOutputPort.delete(storeId, offerId)
    }
}
