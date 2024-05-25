package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CategoryDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.coroutines.runSuspendCatching
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : CategoryInputPort {
    override suspend fun create(category: Category): Result<Unit, Throwable> = runSuspendCatching {
        categoryDatastoreOutputPort.create(category)
    }.onFailure {
        throw it
    }

    override suspend fun update(category: Category): Result<Unit, Throwable> = runSuspendCatching {
        categoryDatastoreOutputPort.update(category)
    }.onFailure {
        throw it
    }

    override suspend fun delete(storeId: UUID, categoryId: UUID): Result<Unit, Throwable> = runSuspendCatching {
        categoryDatastoreOutputPort.delete(storeId, categoryId)
    }.onFailure {
        throw it
    }

    override suspend fun patch(category: Category): Result<Unit, Throwable> = runSuspendCatching {
        categoryDatastoreOutputPort.patch(category)
    }.onFailure {
        throw it
    }
}