package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class UpdateCustomizationOnChildrenInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
) : UpdateCustomizationOnChildrenUseCase {
    override fun execute(storeId: Id, offerId: Id, optionId: Id, customization: Customization) {
        if (!storeDatastoreOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        val offer = offerDatastoreOutputPort
            .findById(storeId, offerId) ?: throw OfferNotFoundException(storeId, offerId)

        val option = offer.findOptionInChildrenById(optionId) ?: throw OptionNotFoundException(optionId)

        option.updateCustomization(customization)

        val productIds = offer.getAllProducts().map { it.id }
        val nonexistentProducts = productDatastoreOutputPort.getIfNotExists(storeId, productIds)
        if (nonexistentProducts.isNotEmpty()) throw ProductsNotFoundException(nonexistentProducts)

        OfferService.validateDuplications(offer)

        offerDatastoreOutputPort.update(storeId, offer)
    }
}
