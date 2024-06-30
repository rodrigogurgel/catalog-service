package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.id
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class OfferDatastorePostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : OfferDatastoreOutputPort {
    companion object {
        private val GET_OFFER_BY_PRODUCT_ID_INCLUDING_CHILDREN = """
            select product_id
            from offer
            where product_id = :product_id
            union 
            select product_id
            from option
            where product_id = :product_id;
        """.trimIndent()
    }

    override fun create(storeId: Id, categoryId: Id, offer: Offer) {
        TODO("Not yet implemented")
    }

    override fun findById(storeId: Id, offerId: Id): Offer? {
        TODO("Not yet implemented")
    }

    override fun exists(offerId: Id): Boolean {
        TODO("Not yet implemented")
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
        return namedParameterJdbcTemplate.query(
            GET_OFFER_BY_PRODUCT_ID_INCLUDING_CHILDREN,
            mapOf("product_id" to productId.value)
        ) { rs, _ -> rs.id() }
    }
}
