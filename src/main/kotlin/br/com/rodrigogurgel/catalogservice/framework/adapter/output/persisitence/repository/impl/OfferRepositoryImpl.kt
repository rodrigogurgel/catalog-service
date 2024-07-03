package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.impl

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.OfferData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.CustomizationRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.OfferRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class OfferRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    private val customizationRepository: CustomizationRepository,
) : OfferRepository {
    companion object {
        private val EXISTS_OFFER_BY_OFFER_ID = """
            select exists(select 1
              from offer
              where offer_id = :offer_id);
        """.trimIndent()

        private val GET_OFFER_BY_PRODUCT_ID_INCLUDING_CHILDREN = """
            select product_id
            from offer
            where product_id = :product_id
            union
            select product_id
            from option
            where product_id = :product_id;
        """.trimIndent()

        private val CREATE_OFFER = """
            insert into offer (offer_id, store_id, name, product_id, category_id, price, status)
            values (:offer_id, :store_id, :name, :product_id, :category_id, :price, :status);
        """.trimIndent()

        private val GET_ALL_PRODUCT_BY_OFFER_ID = """
            with offer_product as (
                select product_id
                from offer
                where offer_id = :offer_id
                union
                select product_id
                from option
                where offer_id = :offer_id
            )
            select *
            from product
            where exists(select 1 from offer_product where offer_id = product_id);
        """.trimIndent()

        private val GET_OFFER = """
            select *
            from offer
            where offer_id = :offer_id
                and store_id = :store_id;
        """.trimIndent()

        private val DELETE_OFFER = """
            delete from offer
            where offer_id = :offer_id
                and store_id = :store_id;
        """.trimIndent()

        private val UPDATE_OFFER = """
            update offer
            set name       = :name,
                product_id = :product_id,
                price      = :price,
                status     = :status
            where store_id = :store_id
              and offer_id = :offer_id;
        """.trimIndent()

        private val EXISTS_BY_OFFER_ID_AND_STORE_ID = """
            select exists(select 1
              from offer
              where offer_id = :offer_id
              and store_id = :store_id);
        """.trimIndent()
    }

    private fun buildParams(offerData: OfferData): Map<String, Any?> {
        return with(offerData) {
            mapOf(
                "offer_id" to offerId,
                "store_id" to storeId,
                "name" to name,
                "product_id" to productId,
                "category_id" to categoryId,
                "price" to price,
                "status" to status.name
            )
        }
    }

    //
//    override fun findById(storeId: Id, offerId: Id): Offer? {
//        val params = mapOf("id" to offerId.value, "store_id" to storeId.value)
//        val productById = namedParameterJdbcTemplate.query(GET_ALL_PRODUCT_BY_OFFER_ID, params, ProductDataMapper())
//            .associateBy { product -> product.id }
//
//        val optionsByCustomizationId =
//            optionDatastorePostgres.getOptions(storeId, offerId, productById)
//
//        val customizationsByOptionId = customizationDatastorePostgres.getCustomizations(
//            storeId,
//            offerId,
//            optionsByCustomizationId
//        )
//
//        optionsByCustomizationId.flatMap { customizationIdToOption -> customizationIdToOption.value }
//            .forEach { option -> option.customizations = customizationsByOptionId[option.id].orEmpty() }
//
//        val customizations = customizationsByOptionId[null] ?: emptyList()
//
//        return namedParameterJdbcTemplate.query(GET_OFFER, params) { rs, _ ->
//            Offer(
//                id = rs.id(),
//                name = rs.name(),
//                product = productById[rs.productId()]!!,
//                price = rs.price(),
//                status = rs.status(),
//                customizations = customizations
//            )
//        }.firstOrNull()
//    }
//
//    override fun exists(offerId: Id): Boolean {
//        return namedParameterJdbcTemplate.queryForObject(
//            EXISTS_OFFER_BY_OFFER_ID,
//            mapOf("id" to offerId.value),
//            Boolean::class.java
//        )!!
//    }
//
//    override fun exists(storeId: Id, offerId: Id): Boolean {
//        return namedParameterJdbcTemplate.queryForObject(
//            EXISTS_BY_OFFER_ID_AND_STORE_ID,
//            mapOf("id" to offerId.value, "store_id" to storeId.value),
//            Boolean::class.java
//        )!!
//    }
//
//    @Transactional
//    override fun update(storeId: Id, offer: Offer) {
//        namedParameterJdbcTemplate.update(UPDATE_OFFER, buildParams(storeId, offer))
//        customizationDatastorePostgres.updateBatch(storeId, offer.id, offer.getCustomizations())
//    }
//
//    override fun delete(storeId: Id, offerId: Id) {
//        namedParameterJdbcTemplate.update(DELETE_OFFER, mapOf("id" to offerId.value, "store_id" to storeId.value))
//    }
//
//    override fun getOffersByProductIdIncludingChildren(productId: Id): List<Id> {
//        return namedParameterJdbcTemplate.query(
//            GET_OFFER_BY_PRODUCT_ID_INCLUDING_CHILDREN,
//            mapOf("product_id" to productId.value)
//        ) { rs, _ -> rs.id() }
//    }
    @Transactional
    override fun create(offerData: OfferData) {
        namedParameterJdbcTemplate.update(CREATE_OFFER, buildParams(offerData))
        customizationRepository.createBatch(offerData.customizations)
    }

    override fun findById(storeId: UUID, offerId: UUID): OfferData? {
        TODO("Not yet implemented")
    }

    override fun exists(offerId: UUID): Boolean {
        val params = mapOf("offer_id" to offerId)
        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_OFFER_BY_OFFER_ID,
            params,
            Boolean::class.java,
        )!!
    }

    override fun exists(storeId: UUID, offerId: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(offerData: OfferData) {
        TODO("Not yet implemented")
    }

    override fun delete(storeId: UUID, offerId: UUID) {
        TODO("Not yet implemented")
    }

    override fun getOffersByProductIdIncludingChildren(productId: UUID): List<UUID> {
        TODO("Not yet implemented")
    }
}