package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.impl

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.CustomizationData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.CustomizationRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.OptionRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Repository

@Repository
class CustomizationRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    private val optionRepository: OptionRepository,
) : CustomizationRepository {

    companion object {
        private val CREATE_CUSTOMIZATION = """
            insert into customization (customization_id, offer_id, store_id, option_id, name, description, min_permitted, max_permitted, status)
            values (:customization_id, :offer_id, :store_id, :option_id, :name, :description, :min_permitted, :max_permitted, :status);
        """.trimIndent()

        private val GET_ALL_CUSTOMIZATION_BY_OFFER_ID = """
            select *
            from customization
            where store_id = :store_id
              and offer_id = :offer_id;
        """.trimIndent()

        private val UPSERT_CUSTOMIZATION = """
            insert into customization (customization_id, offer_id, store_id, option_id, name, description, min_permitted, max_permitted, status)
            values (:customization_id, :offer_id, :store_id, :option_id, :name, :description, :min_permitted, :max_permitted, :status)
            on conflict (customization_id, offer_id) do update
                set name          = :name,
                    description   = :description,
                    min_permitted = :min_permitted,
                    max_permitted = : max_permitted,
                    status        = :status;
        """.trimIndent()

        private val DELETE_IF_NOT_IN = """
            delete
            from customization
            where customization_id not in (select unnest(array [:ids]::uuid[]))
                and offer_id = :offer_id
                and store_id = :store_id;
        """.trimIndent()
    }

    private fun buildParams(
        customizationData: CustomizationData,
    ): Map<String, Any?> {
        return with(customizationData) {
            mapOf(
                "customization_id" to customizationId,
                "offer_id" to offerId,
                "store_id" to storeId,
                "option_id" to optionId,
                "name" to name,
                "description" to description,
                "min_permitted" to minPermitted,
                "max_permitted" to maxPermitted,
                "status" to status.name
            )
        }
    }

    override fun createBatch(customizations: List<CustomizationData>) {
        namedParameterJdbcTemplate.batchUpdate(
            CREATE_CUSTOMIZATION,
            SqlParameterSourceUtils.createBatch(
                customizations.map { customizationData ->
                    buildParams(customizationData)
                }
            )
        )
        customizations.forEach { customizationData ->
            optionRepository.createBatch(customizationData.options)
        }
    }
}