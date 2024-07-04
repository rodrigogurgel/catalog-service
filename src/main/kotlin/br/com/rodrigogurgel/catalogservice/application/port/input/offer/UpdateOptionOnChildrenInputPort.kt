package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateOptionOnChildrenInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : UpdateOptionOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, customizationId: Id, option: Option) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        val offer = offerDatastoreOutputPort
            .findById(storeId, offerId) ?: throw OfferNotFoundException(storeId, offerId)

        val customization =
            offer.findCustomizationInChildrenById(customizationId) ?: throw CustomizationNotFoundException(
                customizationId
            )

        customization.updateOption(option)

        val productIds = offer.getAllProducts().map { it.id }
        val nonexistentProducts = productDatastoreOutputPort.getIfNotExists(productIds)
        if (nonexistentProducts.isNotEmpty()) throw ProductsNotFoundException(nonexistentProducts)

        OfferService.validateDuplications(offer)

        offerDatastoreOutputPort.update(storeId, offer)
    }
}
