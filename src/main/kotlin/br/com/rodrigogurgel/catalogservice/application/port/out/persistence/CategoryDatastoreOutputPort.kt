package br.com.rodrigogurgel.catalogservice.application.port.out.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface CategoryDatastoreOutputPort {
    fun create(storeId: Id, category: Category)
    fun findById(storeId: Id, categoryId: Id): Category?
    fun exists(storeId: Id, categoryId: Id): Boolean
    fun delete(storeId: Id, categoryId: Id)
    fun update(storeId: Id, category: Category)
}
