package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.DeleteOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class DeleteOfferInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val offerOutputPort: OfferOutputPort,
) : DeleteOfferUseCase {
    override fun execute(storeId: Id, offerId: Id) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        return offerOutputPort.delete(storeId, offerId)
    }
}
