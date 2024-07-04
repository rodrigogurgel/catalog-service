package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.impl

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.OfferData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.OfferDataMapper
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.CustomizationRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.OfferRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.OptionRepository
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

    @Transactional
    override fun create(offerData: OfferData) {
        namedParameterJdbcTemplate.update(CREATE_OFFER, buildParams(offerData))
        customizationRepository.createBatch(offerData.customizations)
    }

    override fun findById(storeId: UUID, offerId: UUID): OfferData? {
        val params = mapOf("store_id" to storeId, "offer_id" to offerId)

        val options = optionRepository.getOptionsByOfferId(storeId, offerId)
        val customizations = customizationRepository.getCustomizationsByOfferId(storeId, offerId)

        val customizationOptionsMap = options.groupBy { it.customizationId }

        customizations.forEach { customizationData ->
            customizationData.options = customizationOptionsMap[customizationData.customizationId] ?: emptyList()
        }

        val optionCustomizationsMap = customizations.groupBy { it.optionId }
        options.forEach { optionData ->
            optionData.customizations = optionCustomizationsMap[optionData.optionId] ?: emptyList()
        }

        return namedParameterJdbcTemplate.query(GET_OFFER, params, OfferDataMapper())
            .firstOrNull()
            ?.copy(customizations = customizations.filter { it.optionId == null })
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
}
