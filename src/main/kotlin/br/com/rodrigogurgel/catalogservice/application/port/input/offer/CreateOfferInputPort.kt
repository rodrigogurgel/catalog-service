package br.com.rodrigogurgel.catalogservice.application.port.input.offer

import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.OfferAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CreateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CreateOfferInputPort(
    private val storeRestOutputPort: StoreRestOutputPort,
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
    private val offerDatastoreOutputPort: OfferDatastoreOutputPort,
) : CreateOfferUseCase {
    override fun execute(storeId: Id, categoryId: Id, offer: Offer) {
        if (!storeRestOutputPort.exists(storeId)) throw StoreNotFoundException(storeId)
        if (!categoryDatastoreOutputPort.exists(storeId, categoryId)) throw CategoryNotFoundException(categoryId)
        if (offerDatastoreOutputPort.exists(offer.id)) throw OfferAlreadyExistsException(offer.id)

        val productIds = OfferService.getAllProducts(offer).map { it.id }
        val nonexistentProducts = productDatastoreOutputPort.getIfNotExists(storeId, productIds)
        if (nonexistentProducts.isNotEmpty()) throw ProductsNotFoundException(nonexistentProducts)

        offerDatastoreOutputPort.create(storeId, categoryId, offer)
    }
}
