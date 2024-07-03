//package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence
//
//import br.com.rodrigogurgel.catalogservice.domain.entity.Option
//import br.com.rodrigogurgel.catalogservice.domain.entity.Product
//import br.com.rodrigogurgel.catalogservice.domain.vo.Id
//import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
//import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.id
//import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.price
//import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.productId
//import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.status
//import org.springframework.context.annotation.Lazy
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
//import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
//import org.springframework.stereotype.Repository
//import java.util.UUID
//
//@Repository
//class OptionDatastorePostgres(
//    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
//    @Lazy
//    private val customizationDatastorePostgres: CustomizationDatastorePostgres,
//) {
//    companion object {
//        private val CREATE_OPTION = """
//            insert into option (id, offer_id, store_id, product_id, customization_id, min_permitted, max_permitted, price, status)
//            values (:id, :offer_id, :store_id, :product_id, :customization_id, :min_permitted, :max_permitted, :price, :status);
//        """.trimIndent()
//
//        private val GET_ALL_OPTION_BY_OFFER_ID = """
//            select *
//            from option
//            where store_id = :store_id
//              and offer_id = :offer_id;
//        """.trimIndent()
//
//        private val UPSERT_OPTION = """
//            insert into option (id, offer_id, store_id, product_id, customization_id, min_permitted, max_permitted, price, status)
//            values (:id, :offer_id, :store_id, :product_id, :customization_id, :min_permitted, :max_permitted, :price, :status)
//            on conflict (id, offer_id) do update
//                set product_id = product_id,
//                    price = price,
//                    min_permitted = :min_permitted,
//                    max_permitted = : max_permitted,
//                    status        = :status;
//        """.trimIndent()
//
//        private val DELETE_IF_NOT_IN = """
//            delete
//            from customization
//            where id not in (select unnest(array [:id]::uuid[]))
//                and offer_id = :offer_id
//                and store_id = :store_id;
//        """.trimIndent()
//    }
//
//    private fun buildParams(storeId: Id, offerId: Id, customizationId: Id, option: Option): Map<String, Any?> {
//        return with(option) {
//            mapOf(
//                "id" to option.id.value,
//                "offer_id" to offerId.value,
//                "store_id" to storeId.value,
//                "product_id" to option.product.id.value,
//                "customization_id" to customizationId.value,
//                "min_permitted" to quantity.minPermitted,
//                "max_permitted" to quantity.maxPermitted,
//                "price" to price.normalizedValue().toDouble(),
//                "status" to status.name
//            )
//        }
//    }
//
//    private fun buildParams(storeId: Id, offerId: Id, option: Option): Map<String, Any?> {
//        return with(option) {
//            mapOf(
//                "id" to option.id.value,
//                "offer_id" to offerId.value,
//                "store_id" to storeId.value,
//                "product_id" to option.product.id.value,
//                "min_permitted" to quantity.minPermitted,
//                "max_permitted" to quantity.maxPermitted,
//                "price" to price.normalizedValue().toDouble(),
//                "status" to status.name
//            )
//        }
//    }
//
//    fun createBatch(storeId: Id, offerId: Id, customizationId: Id, options: List<Option>) {
//        namedParameterJdbcTemplate.batchUpdate(
//            CREATE_OPTION,
//            SqlParameterSourceUtils.createBatch(
//                options.map { option ->
//                    buildParams(storeId, offerId, customizationId, option)
//                }
//            )
//        )
//        options.forEach { option ->
//            customizationDatastorePostgres.createBatch(
//                storeId,
//                offerId,
//                option.id,
//                option.getCustomizationsInChildren()
//            )
//        }
//    }
//
//    fun getOptions(storeId: Id, offerId: Id, productById: Map<Id, Product>): Map<Id, List<Option>> {
//        return namedParameterJdbcTemplate
//            .query(
//                GET_ALL_OPTION_BY_OFFER_ID,
//                mapOf("store_id" to storeId.value, "offer_id" to offerId.value)
//            ) { rs, _ ->
//                Id(UUID.fromString(rs.getString("customization_id"))) to Option(
//                    id = rs.id(),
//                    product = productById[rs.productId()]!!,
//                    price = rs.price(),
//                    quantity = Quantity(
//                        minPermitted = rs.getString("min_permitted").toInt(),
//                        maxPermitted = rs.getString("max_permitted").toInt()
//                    ),
//                    status = rs.status(),
//                    customizations = emptyList()
//                )
//            }.groupBy({ it.first }, { it.second })
//    }
//
//    fun updateBatch(storeId: Id, offerId: Id, options: List<Option>) {
//        deleteIfNotIn(storeId, offerId, options.map { option -> option.id })
//        namedParameterJdbcTemplate.batchUpdate(UPSERT_OPTION, SqlParameterSourceUtils.createBatch(
//            options.map { option ->
//                buildParams(storeId, offerId, option)
//            }
//        ))
//    }
//
//    fun deleteIfNotIn(storeId: Id, offerId: Id, optionIds: List<Id>) {
//        namedParameterJdbcTemplate.update(
//            DELETE_IF_NOT_IN, mapOf(
//                "store_id" to storeId.value,
//                "offer_id" to offerId.value,
//                "ids" to optionIds.map { it.value }
//            )
//        )
//    }
//}
