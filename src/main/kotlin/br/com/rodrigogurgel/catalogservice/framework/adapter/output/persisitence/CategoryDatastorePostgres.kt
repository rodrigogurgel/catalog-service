package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.CategoryMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CategoryDatastorePostgres(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : CategoryDatastoreOutputPort {
    companion object {
        private val CREATE_CATEGORY = """
            insert into category (id, store_id, name, description, status)
            values (:id, :store_id, :name, :description, :status);
        """.trimIndent()

        private val GET_CATEGORY = """
            select *
            from category
            where store_id = :store_id
            and id = :id;
        """.trimIndent()

        private val EXISTS_CATEGORY_BY_CATEGORY_ID = """
            select exists(select 1
            from category
            where id = :id);
        """.trimIndent()

        private val EXISTS_CATEGORY_BY_CATEGORY_ID_AND_STORE_ID = """
            select exists(select 1
            from category
            where store_id = :store_id
                and id = :id);
        """.trimIndent()

        private val DELETE_CATEGORY = """
            delete
            from category
            where store_id = :store_id
              and id = :id;
        """.trimIndent()

        private val UPDATE_CATEGORY = """
            update category
            set name        = :name,
                description = :description,
                status      = :status
            where store_id = :store_id
              and id = :id;
        """.trimIndent()

        private val GET_CATEGORIES = """
            select *
            from category
            where store_id = :store_id
              and name ilike (:begins_with)||'%'
            order by name
            limit :limit offset :offset;
        """.trimIndent()

        private val COUNT_CATEGORIES = """
            select count(1)
            from category
            where store_id = :store_id
              and name ilike (:begins_with)||'%'
        """.trimIndent()
    }

    private fun buildParams(storeId: Id, category: Category): Map<String, Any?> {
        return with(category) {
            mapOf(
                "id" to id.value,
                "store_id" to storeId.value,
                "name" to name.value,
                "description" to description?.value,
                "status" to status.name
            )
        }
    }

    override fun create(storeId: Id, category: Category) {
        val params = buildParams(storeId, category)
        namedParameterJdbcTemplate.update(
            CREATE_CATEGORY,
            params,
        )
    }

    override fun findById(storeId: Id, categoryId: Id): Category? {
        val params = mapOf("store_id" to storeId.value, "id" to categoryId.value)
        return namedParameterJdbcTemplate.query(GET_CATEGORY, params, CategoryMapper()).firstOrNull()
    }

    override fun exists(categoryId: Id): Boolean {
        val params = mapOf("id" to categoryId.value)
        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_CATEGORY_BY_CATEGORY_ID,
            params,
            Boolean::class.java
        )!!
    }

    override fun exists(storeId: Id, categoryId: Id): Boolean {
        val params = mapOf("store_id" to storeId.value, "id" to categoryId.value)
        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_CATEGORY_BY_CATEGORY_ID_AND_STORE_ID,
            params,
            Boolean::class.java
        )!!
    }

    override fun delete(storeId: Id, categoryId: Id) {
        val params = mapOf("store_id" to storeId.value, "id" to categoryId.value)
        namedParameterJdbcTemplate.update(DELETE_CATEGORY, params)
    }

    override fun update(storeId: Id, category: Category) {
        val params = buildParams(storeId, category)
        namedParameterJdbcTemplate.update(UPDATE_CATEGORY, params)
    }

    override fun getCategories(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Category> {
        val params = mapOf(
            "store_id" to storeId.value,
            "limit" to limit,
            "offset" to offset,
            "begins_with" to beginsWith.orEmpty()
        )
        return namedParameterJdbcTemplate.query(GET_CATEGORIES, params, CategoryMapper())
    }

    override fun countCategories(storeId: Id, beginsWith: String?): Int {
        val params = mapOf(
            "store_id" to storeId.value,
            "begins_with" to beginsWith.orEmpty()
        )
        return namedParameterJdbcTemplate.queryForObject(
            COUNT_CATEGORIES,
            params,
            Int::class.java
        )!!
    }
}
