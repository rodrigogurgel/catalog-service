package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.idempotency.IdempotencyAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.IdempotencyOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.domain.Idempotency
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    private val idempotencyOutputPort: IdempotencyOutputPort,
) : CategoryInputPort {
    override suspend fun create(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable> =
        idempotencyOutputPort.create(Idempotency.created(idempotencyId, correlationId, category.storeId!!))
            .andThen { categoryDatastoreOutputPort.create(category) }
            .onSuccess {
                idempotencyOutputPort.patch(
                    Idempotency.success(
                        idempotencyId,
                        correlationId,
                        category.storeId,
                    )
                )
            }
            .onFailure { error ->
                when (error) {
                    !is IdempotencyAlreadyExistsException -> {
                        idempotencyOutputPort.patch(
                            Idempotency.failure(
                                idempotencyId,
                                correlationId,
                                category.storeId,
                            )
                        )
                    }
                }
            }

    override suspend fun update(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable> =
        idempotencyOutputPort.create(Idempotency.created(idempotencyId, correlationId, category.storeId!!))
            .andThen { categoryDatastoreOutputPort.update(category) }
            .onSuccess {
                idempotencyOutputPort.patch(
                    Idempotency.success(
                        idempotencyId,
                        correlationId,
                        category.storeId,
                    )
                )
            }
            .onFailure { error ->
                when (error) {
                    !is IdempotencyAlreadyExistsException -> {
                        idempotencyOutputPort.patch(
                            Idempotency.failure(
                                idempotencyId,
                                correlationId,
                                category.storeId,
                            )
                        )
                    }
                }
            }

    override suspend fun delete(
        idempotencyId: UUID,
        correlationId: UUID,
        storeId: UUID,
        categoryId: UUID,
    ): Result<Unit, Throwable> =
        idempotencyOutputPort.create(Idempotency.created(idempotencyId, correlationId, storeId))
            .andThen { categoryDatastoreOutputPort.delete(storeId, categoryId) }
            .onSuccess {
                idempotencyOutputPort.patch(
                    Idempotency.success(idempotencyId, correlationId, storeId)
                )
            }
            .onFailure { error ->
                when (error) {
                    !is IdempotencyAlreadyExistsException -> {
                        idempotencyOutputPort.patch(Idempotency.failure(idempotencyId, correlationId, storeId))
                    }
                }
            }

    override suspend fun patch(idempotencyId: UUID, correlationId: UUID, category: Category): Result<Unit, Throwable> =
        idempotencyOutputPort.create(Idempotency.created(idempotencyId, correlationId, category.categoryId!!))
            .andThen { categoryDatastoreOutputPort.patch(category) }.onSuccess {
                idempotencyOutputPort.patch(
                    Idempotency.success(idempotencyId, correlationId, category.storeId!!)
                )
            }
            .onFailure { error ->
                when (error) {
                    !is IdempotencyAlreadyExistsException -> {
                        idempotencyOutputPort.patch(
                            Idempotency.failure(
                                idempotencyId,
                                correlationId,
                                category.storeId!!
                            )
                        )
                    }
                }
            }
}
