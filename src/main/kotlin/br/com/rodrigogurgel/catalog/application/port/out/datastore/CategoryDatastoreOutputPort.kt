package br.com.rodrigogurgel.catalog.application.port.out.datastore

import br.com.rodrigogurgel.catalog.domain.Category
import java.util.UUID

interface CategoryDatastoreOutputPort {
    fun create(category: Category)
    fun update(category: Category)
    fun delete(storeId: UUID, categoryId: UUID)
    fun patch(category: Category)
}