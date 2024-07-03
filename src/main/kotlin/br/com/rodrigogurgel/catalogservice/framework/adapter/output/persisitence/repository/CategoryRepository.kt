package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.CategoryData
import java.util.UUID

interface CategoryRepository {
    fun create(categoryData: CategoryData)
    fun findById(storeId: UUID, categoryId: UUID): CategoryData?
    fun exists(categoryId: UUID): Boolean
    fun exists(storeId: UUID, categoryId: UUID): Boolean
    fun delete(storeId: UUID, categoryId: UUID)
    fun update(categoryData: CategoryData)
    fun getCategories(storeId: UUID, limit: Int, offset: Int, beginsWith: String?): List<CategoryData>
    fun countCategories(storeId: UUID, beginsWith: String?): Int
}