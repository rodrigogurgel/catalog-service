package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.ProductMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ProductDatastorePostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : ProductDatastoreOutputPort {
    companion object {
        private val CREATE_PRODUCT = """
            insert into product (id, store_id, name, description, image_path)
            values (:id, :store_id, :name, :description, :image_path);
        """.trimIndent()

        private val EXISTS_PRODUCT_BY_PRODUCT_ID = """
            select exists(select 1
            from product
            where id = :id)
        """.trimIndent()

        private val EXISTS_PRODUCT_BY_STORE_ID_AND_PRODUCT_ID = """
            select exists(select 1
            from product
            where id = :id
                and store_id = :store_id)
        """.trimIndent()

        private val UPDATE_PRODUCT = """
            update product
            set name = :name,
                description = :description,
                image_path = :image_path
            where id = :id;
        """.trimIndent()

        private val GET_PRODUCT = """
            select id, store_id, name, description, image_path
            from product
            where id = :id
              and store_id = :store_id;
        """.trimIndent()

        private val DELETE_PRODUCT = """
            delete
            from product
            where id = :id
              and store_id = :store_id;
        """.trimIndent()
    }

    private fun buildParams(storeId: Id, product: Product): Map<String, Any?> {
        return with(product) {
            mapOf(
                "id" to id.value,
                "store_id" to storeId.value,
                "name" to name.value,
                "description" to description?.value,
                "image_path" to image?.path
            )
        }
    }

    override fun create(storeId: Id, product: Product) {
        val params = buildParams(storeId, product)
        namedParameterJdbcTemplate.update(
            CREATE_PRODUCT,
            params,
        )
    }

    override fun findById(storeId: Id, productId: Id): Product? {
        return namedParameterJdbcTemplate.query(
            GET_PRODUCT,
            mapOf("id" to productId.value, "store_id" to storeId.value),
            ProductMapper()
        ).firstOrNull()
    }

    override fun exists(productId: Id): Boolean {
        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_PRODUCT_BY_PRODUCT_ID,
            mapOf("id" to productId.value),
            Boolean::class.java
        )!!
    }

    override fun exists(storeId: Id, productId: Id): Boolean {
        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_PRODUCT_BY_STORE_ID_AND_PRODUCT_ID,
            mapOf("store_id" to storeId.value, "id" to productId.value),
            Boolean::class.java
        )!!
    }

    override fun getIfNotExists(storeId: Id, productIds: List<Id>): List<Id> {
        TODO("Not yet implemented")
    }

    override fun update(storeId: Id, product: Product) {
        namedParameterJdbcTemplate.update(UPDATE_PRODUCT, buildParams(storeId, product))
    }

    override fun delete(storeId: Id, productId: Id) {
        namedParameterJdbcTemplate.update(
            DELETE_PRODUCT,
            mapOf("store_id" to storeId.value, "id" to productId.value),
        )
    }
}
