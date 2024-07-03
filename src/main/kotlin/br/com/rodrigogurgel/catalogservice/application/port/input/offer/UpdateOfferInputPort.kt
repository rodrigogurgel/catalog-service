package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateOfferInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val productOutputPort: ProductOutputPort,
    private val offerOutputPort: OfferOutputPort,
) : UpdateOfferUseCase {
    override fun execute(storeId: Id, offer: Offer) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!offerOutputPort.exists(storeId, offer.id)) throw OfferNotFoundException(storeId, offer.id)

        val productIds = OfferService.getAllProducts(offer).map { it.id }
        val nonexistentProducts = productOutputPort.getIfNotExists(storeId, productIds)
        if (nonexistentProducts.isNotEmpty()) throw ProductsNotFoundException(nonexistentProducts)

        OfferService.validateDuplications(offer)

        offerOutputPort.update(storeId, offer)
    }
}
