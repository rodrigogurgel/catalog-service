package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateOfferInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
) : UpdateOfferUseCase {
    override fun execute(storeId: Id, offer: Offer) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!offerDatastoreOutputPort.exists(storeId, offer.id)) throw OfferNotFoundException(storeId, offer.id)

        val productIds = offer.getAllProducts().map { it.id }
        val nonexistentProducts = productDatastoreOutputPort.getIfNotExists(productIds)
        if (nonexistentProducts.isNotEmpty()) throw ProductsNotFoundException(nonexistentProducts)

        OfferService.validateDuplications(offer)

        offerDatastoreOutputPort.update(storeId, offer)
    }
}
