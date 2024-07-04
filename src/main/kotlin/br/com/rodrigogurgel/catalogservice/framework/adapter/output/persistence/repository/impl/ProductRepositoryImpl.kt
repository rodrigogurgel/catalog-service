package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.impl

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data.ProductData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.mapper.ProductDataMapper
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.ProductRepository
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.utils.getUUID
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProductRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : ProductRepository {
    companion object {
        private val CREATE_PRODUCT = """
            insert into product (product_id, store_id, name, description, image_path)
            values (:product_id, :store_id, :name, :description, :image_path);
        """.trimIndent()

        private val EXISTS_PRODUCT_BY_PRODUCT_ID = """
            select exists(select 1
            from product
            where product_id = :product_id)
        """.trimIndent()

        private val EXISTS_PRODUCT_BY_STORE_ID_AND_PRODUCT_ID = """
            select exists(select 1
            from product
            where product_id = :product_id
                and store_id = :store_id)
        """.trimIndent()

        private val UPDATE_PRODUCT = """
            update product
            set name = :name,
                description = :description,
                image_path = :image_path
            where product_id = :product_id;
        """.trimIndent()

        private val GET_PRODUCT = """
            select product_id, store_id, name, description, image_path
            from product
            where product_id = :product_id
              and store_id = :store_id;
        """.trimIndent()

        private val DELETE_PRODUCT = """
            delete
            from product
            where product_id = :product_id
              and store_id = :store_id;
        """.trimIndent()

        private val GET_PRODUCTS = """
            select *
            from product
            where store_id = :store_id
              and name ilike (coalesce(:begins_with, ''))||'%'
            order by name
            limit :limit offset :offset;
        """.trimIndent()

        private val COUNT_PRODUCTS = """
            select count(1)
            from product
            where store_id = :store_id
              and name ilike (coalesce(:begins_with, ''))||'%'
        """.trimIndent()

        private val GET_IF_NOT_EXISTS = """
            with offer_product as (select unnest(array[:product_ids]) as product_id)
            select product_id as product_id from offer_product
            where not exists(select 1 from product where product_id = product_id)
        """.trimIndent()

        private val PRODUCT_IS_IN_USE = """
            select exists(select 1
                from offer
                where product_id = :product_id
                union
                select 1
                from option
                where product_id = :product_id);
        """.trimIndent()

        private val GET_ALL_PRODUCT_BY_OFFER_IDS = """
            select product.*
            from product
                     inner join offer on product.product_id = offer.product_id
            where offer_id in (select unnest(array [:offer_ids]::uuid[]));
        """.trimIndent()
    }

    private fun buildParams(productData: ProductData): Map<String, Any?> {
        return with(productData) {
            mapOf(
                "product_id" to productId,
                "store_id" to storeId,
                "name" to name,
                "description" to description,
                "image_path" to imagePath
            )
        }
    }

    override fun create(productData: ProductData) {
        val params = buildParams(productData)
        namedParameterJdbcTemplate.update(
            CREATE_PRODUCT,
            params,
        )
    }

    override fun findById(storeId: UUID, productId: UUID): ProductData? {
        val params = mapOf("product_id" to productId, "store_id" to storeId)

        return namedParameterJdbcTemplate.query(
            GET_PRODUCT,
            params,
            ProductDataMapper()
        ).firstOrNull()
    }

    override fun exists(productId: UUID): Boolean {
        val params = mapOf("product_id" to productId)

        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_PRODUCT_BY_PRODUCT_ID,
            params,
            Boolean::class.java
        )!!
    }

    override fun exists(storeId: UUID, productId: UUID): Boolean {
        val params = mapOf("store_id" to storeId, "product_id" to productId)

        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_PRODUCT_BY_STORE_ID_AND_PRODUCT_ID,
            params,
            Boolean::class.java
        )!!
    }

    override fun getIfNotExists(storeId: UUID, productIds: List<UUID>): List<UUID> {
        val params = mapOf("product_ids" to productIds.map { productId -> productId })

        return namedParameterJdbcTemplate.query(
            GET_IF_NOT_EXISTS,
            params
        ) { rs, _ ->
            rs.getUUID("product_id")
        }
    }

    override fun update(productData: ProductData) {
        val params = buildParams(productData)
        namedParameterJdbcTemplate.update(UPDATE_PRODUCT, params)
    }

    override fun delete(storeId: UUID, productId: UUID) {
        val params = mapOf("store_id" to storeId, "product_id" to productId)
        namedParameterJdbcTemplate.update(
            DELETE_PRODUCT,
            params
        )
    }

    override fun getProducts(storeId: UUID, limit: Int, offset: Int, beginsWith: String?): List<ProductData> {
        val params = mapOf(
            "store_id" to storeId,
            "limit" to limit,
            "offset" to offset,
            "begins_with" to beginsWith.orEmpty()
        )
        return namedParameterJdbcTemplate.query(GET_PRODUCTS, params, ProductDataMapper())
    }

    override fun countProducts(storeId: UUID, beginsWith: String?): Int {
        val params = mapOf(
            "store_id" to storeId,
            "begins_with" to beginsWith.orEmpty()
        )
        return namedParameterJdbcTemplate.queryForObject(
            COUNT_PRODUCTS,
            params,
            Int::class.java
        )!!
    }

    override fun productIsInUse(productId: UUID): Boolean {
        val params = mapOf("product_id" to productId)

        return namedParameterJdbcTemplate.queryForObject(
            PRODUCT_IS_IN_USE,
            params,
            Boolean::class.java
        )!!
    }

    override fun getAllProductByOfferIds(offerIds: List<UUID>): List<ProductData> {
        val params = mapOf("offer_ids" to offerIds)
        return namedParameterJdbcTemplate.query(GET_ALL_PRODUCT_BY_OFFER_IDS, params, ProductDataMapper())
    }
}
