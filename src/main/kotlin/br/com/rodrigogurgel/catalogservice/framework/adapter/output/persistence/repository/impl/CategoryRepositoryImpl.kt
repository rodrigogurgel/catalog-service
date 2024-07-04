package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.impl

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data.CategoryData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.mapper.CategoryDataMapper
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.repository.CategoryRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CategoryRepositoryImpl(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
) : CategoryRepository {
    companion object {
        private val CREATE_CATEGORY = """
            insert into category (category_id, store_id, name, description, status)
            values (:category_id, :store_id, :name, :description, :status);
        """.trimIndent()

        private val GET_CATEGORY = """
            select *
            from category
            where store_id = :store_id
            and category_id = :category_id;
        """.trimIndent()

        private val EXISTS_CATEGORY_BY_CATEGORY_ID = """
            select exists(select 1
            from category
            where category_id = :category_id);
        """.trimIndent()

        private val EXISTS_CATEGORY_BY_CATEGORY_ID_AND_STORE_ID = """
            select exists(select 1
            from category
            where store_id = :store_id
                and category_id = :category_id);
        """.trimIndent()

        private val DELETE_CATEGORY = """
            delete
            from category
            where store_id = :store_id
              and category_id = :category_id;
        """.trimIndent()

        private val UPDATE_CATEGORY = """
            update category
            set name        = :name,
                description = :description,
                status      = :status
            where store_id = :store_id
              and category_id = :category_id;
        """.trimIndent()

        private val GET_CATEGORIES = """
            select *
            from category
            where store_id = :store_id
              and name ilike (coalesce(:begins_with, ''))||'%'
            order by name
            limit :limit offset :offset;
        """.trimIndent()

        private val COUNT_CATEGORIES = """
            select count(1)
            from category
            where store_id = :store_id
              and name ilike (coalesce(:begins_with, ''))||'%'
        """.trimIndent()
    }

    private fun buildParams(categoryData: CategoryData): Map<String, Any?> {
        return with(categoryData) {
            mapOf(
                "category_id" to categoryId,
                "store_id" to storeId,
                "name" to name,
                "description" to description,
                "status" to status.name
            )
        }
    }

    override fun create(categoryData: CategoryData) {
        val params = buildParams(categoryData)
        namedParameterJdbcTemplate.update(
            CREATE_CATEGORY,
            params,
        )
    }

    override fun findById(storeId: UUID, categoryId: UUID): CategoryData? {
        val params = mapOf("store_id" to storeId, "category_id" to categoryId)
        return namedParameterJdbcTemplate.query(GET_CATEGORY, params, CategoryDataMapper())
            .firstOrNull()
    }

    override fun exists(categoryId: UUID): Boolean {
        val params = mapOf("category_id" to categoryId)
        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_CATEGORY_BY_CATEGORY_ID,
            params,
            Boolean::class.java
        )!!
    }

    override fun exists(storeId: UUID, categoryId: UUID): Boolean {
        val params = mapOf("store_id" to storeId, "category_id" to categoryId)
        return namedParameterJdbcTemplate.queryForObject(
            EXISTS_CATEGORY_BY_CATEGORY_ID_AND_STORE_ID,
            params,
            Boolean::class.java
        )!!
    }

    override fun delete(storeId: UUID, categoryId: UUID) {
        val params = mapOf("store_id" to storeId, "category_id" to categoryId)
        namedParameterJdbcTemplate.update(DELETE_CATEGORY, params)
    }

    override fun update(categoryData: CategoryData) {
        val params = buildParams(categoryData)
        namedParameterJdbcTemplate.update(UPDATE_CATEGORY, params)
    }

    override fun getCategories(storeId: UUID, limit: Int, offset: Int, beginsWith: String?): List<CategoryData> {
        val params = mapOf(
            "store_id" to storeId,
            "limit" to limit,
            "offset" to offset,
            "begins_with" to beginsWith.orEmpty()
        )
        return namedParameterJdbcTemplate.query(GET_CATEGORIES, params, CategoryDataMapper())
    }

    override fun countCategories(storeId: UUID, beginsWith: String?): Int {
        val params = mapOf(
            "store_id" to storeId,
            "begins_with" to beginsWith.orEmpty()
        )
        return namedParameterJdbcTemplate.queryForObject(
            COUNT_CATEGORIES,
            params,
            Int::class.java
        )!!
    }
}
