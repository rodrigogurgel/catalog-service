package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.impl

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.OptionData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.OptionDataMapper
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.CustomizationRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.OptionRepository
import org.springframework.context.annotation.Lazy
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OptionRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    @Lazy
    private val customizationRepository: CustomizationRepository,
) : OptionRepository {
    companion object {
        private val CREATE_OPTION = """
            insert into option (option_id, offer_id, store_id, product_id, customization_id, min_permitted, max_permitted, price, status)
            values (:option_id, :offer_id, :store_id, :product_id, :customization_id, :min_permitted, :max_permitted, :price, :status);
        """.trimIndent()

        private val GET_ALL_OPTION_BY_OFFER_ID = """
            select *
            from option
            where offer_id = :offer_id
                and store_id = :store_id;
        """.trimIndent()

        private val UPSERT_OPTION = """
            insert into option (option_id, offer_id, store_id, product_id, customization_id, min_permitted, max_permitted, price, status)
            values (:option_id, :offer_id, :store_id, :product_id, :customization_id, :min_permitted, :max_permitted, :price, :status)
            on conflict (option_id, offer_id) do update
                set product_id = :product_id,
                    price = :price,
                    min_permitted = :min_permitted,
                    max_permitted = :max_permitted,
                    status        = :status;
        """.trimIndent()

        private val DELETE_IF_NOT_IN = """
            delete
            from customization
            where option_id not in (select unnest(array [:option_ids]::uuid[]))
                and offer_id = :offer_id
                and store_id = :store_id;
        """.trimIndent()
    }

    private fun buildParams(optionData: OptionData): Map<String, Any?> {
        return with(optionData) {
            mapOf(
                "option_id" to optionId,
                "offer_id" to offerId,
                "store_id" to storeId,
                "product_id" to productId,
                "customization_id" to customizationId,
                "min_permitted" to minPermitted,
                "max_permitted" to maxPermitted,
                "price" to price,
                "status" to status.name
            )
        }
    }

    override fun createBatch(options: List<OptionData>) {
        namedParameterJdbcTemplate.batchUpdate(
            CREATE_OPTION,
            SqlParameterSourceUtils.createBatch(
                options.map { optionData ->
                    buildParams(optionData)
                }
            )
        )
        options.forEach { optionData ->
            customizationRepository.createBatch(
                optionData.customizations
            )
        }
    }

    override fun getOptionsByOfferId(storeId: UUID, offerId: UUID): List<OptionData> {
        val params = mapOf("store_id" to storeId, "offer_id" to offerId)
        return namedParameterJdbcTemplate.query(GET_ALL_OPTION_BY_OFFER_ID, params, OptionDataMapper())
    }

    override fun updateBatch(options: List<OptionData>) {
        namedParameterJdbcTemplate.batchUpdate(
            UPSERT_OPTION,
            SqlParameterSourceUtils.createBatch(
                options.map { optionData ->
                    buildParams(optionData)
                }
            )
        )
    }

    override fun deleteIfNotIn(storeId: UUID, offerId: UUID, optionIds: List<UUID>) {
        namedParameterJdbcTemplate.update(
            DELETE_IF_NOT_IN,
            mapOf(
                "store_id" to storeId,
                "offer_id" to offerId,
                "option_ids" to optionIds
            )
        )
    }
}
