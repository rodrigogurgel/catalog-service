package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Category
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CategoryService(
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : CategoryInputPort {
    override suspend fun create(category: Category): Result<Unit, Throwable> =
        categoryDatastoreOutputPort.create(category)

    override suspend fun update(category: Category): Result<Unit, Throwable> =
        categoryDatastoreOutputPort.update(category)

    override suspend fun delete(storeId: UUID, categoryId: UUID): Result<Unit, Throwable> =
        categoryDatastoreOutputPort.delete(storeId, categoryId)

    override suspend fun patch(category: Category): Result<Unit, Throwable> =
        categoryDatastoreOutputPort.patch(category)
}
