package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class AddCustomizationInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val offerOutputPort: OfferOutputPort,
    private val productOutputPort: ProductOutputPort
) : AddCustomizationUseCase {

    override fun execute(storeId: Id, offerId: Id, customization: Customization) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)

        val offer = offerOutputPort
            .findById(storeId, offerId) ?: throw OfferNotFoundException(storeId, offerId)

        offer.addCustomization(customization)

        val productIds = OfferService.getAllProducts(offer).map { it.id }
        val nonexistentProducts = productOutputPort.getIfNotExists(storeId, productIds)
        if (nonexistentProducts.isNotEmpty()) throw ProductsNotFoundException(nonexistentProducts)

        OfferService.validateDuplications(offer)

        offerOutputPort.update(storeId, offer)
    }
}
