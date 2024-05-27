package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.IdempotencyInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Category
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CategoryService(
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    private val idempotencyInputPort: IdempotencyInputPort,
) : CategoryInputPort {
    override suspend fun create(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            category.storeId!!
        ) { categoryDatastoreOutputPort.create(category) }

    override suspend fun update(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            category.storeId!!
        ) { categoryDatastoreOutputPort.update(category) }

    override suspend fun delete(
        idempotencyId: UUID,
        correlationId: UUID,
        category: Category,
    ): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            category.storeId!!
        ) {
            categoryDatastoreOutputPort.delete(
                category.storeId,
                category.categoryId!!
            )
        }

    override suspend fun patch(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            category.storeId!!
        ) { categoryDatastoreOutputPort.patch(category) }
}
