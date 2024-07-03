package br.com.rodrigogurgel.catalogservice.application.port.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface CategoryOutputPort {
    fun create(storeId: Id, category: Category)
    fun findById(storeId: Id, categoryId: Id): Category?
    fun exists(categoryId: Id): Boolean
    fun exists(storeId: Id, categoryId: Id): Boolean
    fun delete(storeId: Id, categoryId: Id)
    fun update(storeId: Id, category: Category)
    fun getCategories(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Category>
    fun countCategories(storeId: Id, beginsWith: String?): Int
}
