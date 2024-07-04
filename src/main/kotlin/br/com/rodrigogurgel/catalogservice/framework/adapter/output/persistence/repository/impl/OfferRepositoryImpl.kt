package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.impl

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data.CustomizationData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data.OfferData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data.OptionData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.mapper.OfferDataMapper
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.CustomizationRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.OfferRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.OptionRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class OfferRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    private val customizationRepository: CustomizationRepository,
    private val optionRepository: OptionRepository,
) : OfferRepository {
    companion object {
        private val EXISTS_OFFER_BY_OFFER_ID = """
            select exists(select 1
              from offer
              where offer_id = :offer_id);
        """.trimIndent()

        private val CREATE_OFFER = """
            insert into offer (offer_id, store_id, name, product_id, category_id, price, status)
            values (:offer_id, :store_id, :name, :product_id, :category_id, :price, :status);
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

        private val COUNT_OFFERS = """
            select count(1)
            from offer
            where store_id = :store_id
              and category_id = :category_id
              and name ilike (coalesce(:begins_with, ''))||'%';
        """.trimIndent()

        private val GET_OFFERS = """
            select *
            from offer
            where store_id = :store_id
              and category_id = :category_id
              and name ilike (coalesce(:begins_with, ''))||'%'
            order by name
            limit :limit offset :offset;
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

    private fun buildOffer(
        offer: OfferData,
        customizations: List<CustomizationData>,
        options: List<OptionData>,
    ): OfferData {
        val customizationOptionsMap = options.groupBy { it.customizationId }

        customizations.forEach { customizationData ->
            customizationData.options = customizationOptionsMap[customizationData.customizationId] ?: emptyList()
        }

        val optionCustomizationsMap = customizations.groupBy { it.optionId }
        options.forEach { optionData ->
            optionData.customizations = optionCustomizationsMap[optionData.optionId] ?: emptyList()
        }

        return offer.copy(customizations = customizations.filter { it.optionId == null })
    }

    @Transactional
    override fun create(offerData: OfferData) {
        namedParameterJdbcTemplate.update(CREATE_OFFER, buildParams(offerData))
        customizationRepository.createBatch(offerData.customizations)
    }

    override fun findById(storeId: UUID, offerId: UUID): OfferData? {
        val params = mapOf("store_id" to storeId, "offer_id" to offerId)

        val options = optionRepository.getOptionsByOfferIds(storeId, listOf(offerId))
        val customizations = customizationRepository.getCustomizationsByOfferIds(storeId, listOf(offerId))

        return namedParameterJdbcTemplate.query(GET_OFFER, params, OfferDataMapper())
            .firstOrNull()
            ?.let { offerData -> buildOffer(offerData, customizations, options) }
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
        val params = mapOf("store_id" to storeId, "offer_id" to offerId)
        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_BY_OFFER_ID_AND_STORE_ID,
            params,
            Boolean::class.java,
        )!!
    }

    @Transactional
    override fun update(offerData: OfferData) {
        namedParameterJdbcTemplate.update(UPDATE_OFFER, buildParams(offerData))
        val customizations = offerData.getAllCustomizationsInChildren()
        val options = offerData.getAllOptionsInChildren()

        customizationRepository.updateBatch(customizations)
        optionRepository.updateBatch(options)
        customizationRepository.deleteIfNotIn(
            offerData.storeId,
            offerData.offerId,
            customizations.map { customizationData -> customizationData.customizationId }
        )
        optionRepository.deleteIfNotIn(
            offerData.storeId,
            offerData.offerId,
            options.map { optionData -> optionData.optionId }
        )
    }

    override fun delete(storeId: UUID, offerId: UUID) {
        val params = mapOf("store_id" to storeId, "offer_id" to offerId)
        namedParameterJdbcTemplate.update(DELETE_OFFER, params)
    }

    override fun countOffers(storeId: UUID, categoryId: UUID, beginsWith: String?): Int {
        val params = mapOf("store_id" to storeId, "category_id" to categoryId, "begins_with" to beginsWith)
        return namedParameterJdbcTemplate.queryForObject(
            COUNT_OFFERS,
            params,
            Int::class.java,
        )!!
    }

    override fun getOffers(
        storeId: UUID,
        categoryId: UUID,
        limit: Int,
        offset: Int,
        beginsWith: String?,
    ): List<OfferData> {
        val params = mapOf(
            "store_id" to storeId,
            "category_id" to categoryId,
            "begins_with" to beginsWith,
            "limit" to limit,
            "offset" to offset
        )

        val offers = namedParameterJdbcTemplate.query(GET_OFFERS, params, OfferDataMapper())
        val options = optionRepository.getOptionsByOfferIds(storeId, offers.map { offerData -> offerData.offerId })
            .groupBy { optionData -> optionData.offerId }
        val customizations =
            customizationRepository.getCustomizationsByOfferIds(storeId, offers.map { offerData -> offerData.offerId })
                .groupBy { customizationData -> customizationData.offerId }

        return offers.map { offerData ->
            buildOffer(
                offerData,
                customizations[offerData.offerId].orEmpty(),
                options[offerData.offerId].orEmpty()
            )
        }
    }
}
