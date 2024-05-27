package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Category
import com.github.michaelbull.result.Result
import java.util.UUID

interface CategoryDatastoreOutputPort {
    suspend fun create(category: Category): Result<Unit, Throwable>
    suspend fun update(category: Category): Result<Unit, Throwable>
    suspend fun delete(storeId: UUID, categoryId: UUID): Result<Unit, Throwable>
    suspend fun patch(category: Category): Result<Unit, Throwable>
}
