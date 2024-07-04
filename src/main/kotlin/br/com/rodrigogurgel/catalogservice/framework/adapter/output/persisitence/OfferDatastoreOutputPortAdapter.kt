package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.toData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.OfferRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.ProductRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OfferDatastoreOutputPortAdapter(
    private val offerRepository: OfferRepository,
    private val productRepository: ProductRepository,
) : OfferDatastoreOutputPort {
    override fun create(storeId: Id, categoryId: Id, offer: Offer) {
        offerRepository.create(offer.toData(storeId.value, categoryId.value))
    }

    override fun findById(storeId: Id, offerId: Id): Offer? {
        val productById = productRepository.getAllProductByOfferId(offerId.value)
            .map { productData -> productData.toEntity() }
            .associateBy { product -> product.id }
        return offerRepository.findById(storeId.value, offerId.value)?.toEntity(productById)
    }

    override fun exists(offerId: Id): Boolean {
        return offerRepository.exists(offerId.value)
    }

    override fun exists(storeId: Id, offerId: Id): Boolean {
        return offerRepository.exists(offerId.value, storeId.value)
    }

    override fun update(storeId: Id, offer: Offer) {
        offerRepository.update(offer.toData(storeId.value, UUID.randomUUID()))
    }

    override fun delete(storeId: Id, offerId: Id) {
        offerRepository.delete(offerId.value, offerId.value)
    }
}
