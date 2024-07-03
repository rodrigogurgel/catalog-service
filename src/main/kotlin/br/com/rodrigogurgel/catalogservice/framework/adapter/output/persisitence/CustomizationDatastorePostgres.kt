//package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence
//
//import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
//import br.com.rodrigogurgel.catalogservice.domain.entity.Option
//import br.com.rodrigogurgel.catalogservice.domain.vo.Id
//import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
//import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
//import org.springframework.stereotype.Repository
//import java.util.UUID
//
//@Repository
//class CustomizationDatastorePostgres(
//    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
//    private val optionDatastorePostgres: OptionDatastorePostgres,
//) {
//    companion object {
//        private val CREATE_CUSTOMIZATION = """
//            insert into customization (id, offer_id, store_id, option_id, name, description, min_permitted, max_permitted, status)
//            values (:id, :offer_id, :store_id, :option_id, :name, :description, :min_permitted, :max_permitted, :status);
//        """.trimIndent()
//
//        private val GET_ALL_CUSTOMIZATION_BY_OFFER_ID = """
//            select *
//            from customization
//            where store_id = :store_id
//              and offer_id = :offer_id;
//        """.trimIndent()
//
//        private val UPSERT_CUSTOMIZATION = """
//            insert into customization (id, offer_id, store_id, option_id, name, description, min_permitted, max_permitted, status)
//            values (:id, :offer_id, :store_id, :option_id, :name, :description, :min_permitted, :max_permitted, :status)
//            on conflict (id, offer_id) do update
//                set name          = :name,
//                    description   = :description,
//                    min_permitted = :min_permitted,
//                    max_permitted = : max_permitted,
//                    status        = :status;
//        """.trimIndent()
//
//        private val DELETE_IF_NOT_IN = """
//            delete
//            from customization
//            where id not in (select unnest(array [:ids]::uuid[]))
//                and offer_id = :offer_id
//                and store_id = :store_id;
//        """.trimIndent()
//    }
//
//    private fun buildParams(
//        storeId: Id,
//        offerId: Id,
//        optionId: Id?,
//        customization: Customization,
//    ): Map<String, Any?> {
//        return with(customization) {
//            mapOf(
//                "id" to id.value,
//                "offer_id" to offerId.value,
//                "store_id" to storeId.value,
//                "option_id" to optionId?.value,
//                "name" to name.value,
//                "description" to description?.value,
//                "min_permitted" to quantity.minPermitted,
//                "max_permitted" to quantity.maxPermitted,
//                "status" to status.name
//            )
//        }
//    }
//
//    private fun buildParams(
//        storeId: Id,
//        offerId: Id,
//        customization: Customization,
//    ): Map<String, Any?> {
//        return with(customization) {
//            mapOf(
//                "id" to id.value,
//                "offer_id" to offerId.value,
//                "store_id" to storeId.value,
//                "name" to name.value,
//                "description" to description?.value,
//                "min_permitted" to quantity.minPermitted,
//                "max_permitted" to quantity.maxPermitted,
//                "status" to status.name
//            )
//        }
//    }
//
//    fun createBatch(storeId: Id, offerId: Id, optionId: Id?, customizations: List<Customization>) {
//        namedParameterJdbcTemplate.batchUpdate(
//            CREATE_CUSTOMIZATION,
//            SqlParameterSourceUtils.createBatch(
//                customizations.map { customization ->
//                    buildParams(storeId, offerId, optionId, customization)
//                }
//            )
//        )
//        customizations.forEach { customization ->
//            optionDatastorePostgres.createBatch(
//                storeId,
//                offerId,
//                customization.id,
//                customization.options
//            )
//        }
//    }
//
//    fun getCustomizations(
//        storeId: Id,
//        offerId: Id,
//        optionsByCustomizationId: Map<Id, List<Option>>,
//    ): Map<Id?, List<Customization>> {
//        val params = mapOf("store_id" to storeId.value, "offer_id" to offerId.value)
//        return namedParameterJdbcTemplate
//            .query(GET_ALL_CUSTOMIZATION_BY_OFFER_ID, params) { rs, _ ->
//                rs.getString("option_id")?.let { Id(UUID.fromString(it)) } to Customization(
//                    id = rs.id(),
//                    name = rs.name(),
//                    description = rs.description(),
//                    quantity = Quantity(
//                        minPermitted = rs.getString("min_permitted").toInt(),
//                        maxPermitted = rs.getString("max_permitted").toInt()
//                    ),
//                    status = rs.status(),
//                    options = optionsByCustomizationId[rs.id()].orEmpty()
//                )
//            }.groupBy({ it.first }, { it.second })
//    }
//
//    fun updateBatch(storeId: Id, offerId: Id, customizations: List<Customization>) {
//        deleteIfNotIn(storeId, offerId, customizations.map { customization -> customization.id })
//        namedParameterJdbcTemplate.batchUpdate(UPSERT_CUSTOMIZATION, SqlParameterSourceUtils.createBatch(
//            customizations.map { customization ->
//                buildParams(storeId, offerId, customization)
//            }
//        ))
//    }
//
//    fun deleteIfNotIn(storeId: Id, offerId: Id, customizationIds: List<Id>) {
//        namedParameterJdbcTemplate.update(
//            DELETE_IF_NOT_IN, mapOf(
//                "store_id" to storeId.value,
//                "offer_id" to offerId.value,
//                "ids" to customizationIds.map { it.value }
//            )
//        )
//    }
//}
