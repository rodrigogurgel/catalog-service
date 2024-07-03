package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.OfferAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CreateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CreateOfferInputPort(
    private val storeOutputPort: StoreOutputPort,
    private val categoryOutputPort: CategoryOutputPort,
    private val productOutputPort: ProductOutputPort,
    private val offerOutputPort: OfferOutputPort,
) : CreateOfferUseCase {
    override fun execute(storeId: Id, categoryId: Id, offer: Offer) {
        if (!storeOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryOutputPort.exists(storeId, categoryId)) {
            throw CategoryNotFoundException(
                storeId,
                categoryId
            )
        }
        if (offerOutputPort.exists(offer.id)) throw OfferAlreadyExistsException(offer.id)

        val productIds = OfferService.getAllProducts(offer).map { it.id }
        val nonexistentProducts = productOutputPort.getIfNotExists(storeId, productIds)
        if (nonexistentProducts.isNotEmpty()) throw ProductsNotFoundException(nonexistentProducts)

        OfferService.validateDuplications(offer)

        offerOutputPort.create(storeId, categoryId, offer)
    }
}
