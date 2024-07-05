package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.mapper.toData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.mapper.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.OfferRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.ProductRepository
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
        val productById = productRepository.getAllProductByOfferIds(listOf(offerId.value))
            .associateBy { productData -> productData.productId }

        return offerRepository.findById(storeId.value, offerId.value)?.toEntity(productById)
    }

    override fun exists(offerId: Id): Boolean {
        return offerRepository.exists(offerId.value)
    }

    override fun exists(storeId: Id, offerId: Id): Boolean {
        return offerRepository.exists(storeId.value, offerId.value)
    }

    override fun update(storeId: Id, offer: Offer) {
        offerRepository.update(offer.toData(storeId.value, UUID.randomUUID()))
    }

    override fun delete(storeId: Id, offerId: Id) {
        offerRepository.delete(storeId.value, offerId.value)
    }

    override fun getOffers(storeId: Id, categoryId: Id, limit: Int, offset: Int, beginsWith: String?): List<Offer> {
        val offers = offerRepository.getOffers(storeId.value, categoryId.value, limit, offset, beginsWith?.trim())

        val products = productRepository.getAllProductByOfferIds(offers.map { offerData -> offerData.offerId })
            .associateBy { productData ->
                productData.productId
            }
        return offers.map { offerData -> offerData.toEntity(products) }
    }

    override fun countOffers(storeId: Id, categoryId: Id, beginsWith: String?): Int {
        return offerRepository.countOffers(storeId.value, categoryId.value, beginsWith)
    }
}
