package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Category
import java.util.UUID

interface CategoryDatastoreOutputPort {
    suspend fun create(category: Category)
    suspend fun update(category: Category)
    suspend fun delete(storeId: UUID, categoryId: UUID)
    suspend fun patch(category: Category)
}