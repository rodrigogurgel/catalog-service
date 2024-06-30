package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import org.springframework.stereotype.Repository

@Repository
class CategoryDatastorePostgres : CategoryDatastoreOutputPort {
    override fun create(storeId: Id, category: Category) {
        TODO("Not yet implemented")
    }

    override fun findById(storeId: Id, categoryId: Id): Category? {
        TODO("Not yet implemented")
    }

    override fun exists(categoryId: Id): Boolean {
        TODO("Not yet implemented")
    }

    override fun exists(storeId: Id, categoryId: Id): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(storeId: Id, categoryId: Id) {
        TODO("Not yet implemented")
    }

    override fun update(storeId: Id, category: Category) {
        TODO("Not yet implemented")
    }
}
