package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.OfferDataMapper.Companion.toData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.OfferRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class OfferOutputPortAdapter(
    private val offerRepository: OfferRepository,
) : OfferOutputPort {
    override fun create(storeId: Id, categoryId: Id, offer: Offer) {
        offerRepository.create(offer.toData(storeId.value, categoryId.value,))
    }

    override fun findById(storeId: Id, offerId: Id): Offer? {
        TODO("Not yet implemented")
    }

    override fun exists(offerId: Id): Boolean {
        return offerRepository.exists(offerId.value)
    }

    override fun exists(storeId: Id, offerId: Id): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(storeId: Id, offer: Offer) {
        TODO("Not yet implemented")
    }

    override fun delete(storeId: Id, offerId: Id) {
        TODO("Not yet implemented")
    }

    override fun getOffersByProductIdIncludingChildren(productId: Id): List<Id> {
        TODO("Not yet implemented")
    }

}
