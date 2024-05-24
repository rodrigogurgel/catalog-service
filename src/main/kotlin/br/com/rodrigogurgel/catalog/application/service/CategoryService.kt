package br.com.rodrigogurgel.catalog.application.service

import br.com.rodrigogurgel.catalog.domain.Category
import br.com.rodrigogurgel.catalog.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalog.application.port.out.datastore.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalog.application.port.out.events.CategoryEventOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.github.michaelbull.result.runCatching
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
) : CategoryInputPort {

    private val logger = LoggerFactory.getLogger(CategoryService::class.java)

    override fun create(category: Category): Result<Unit, Throwable> = runCatching {
        categoryDatastoreOutputPort.create(category)
    }.onFailure {
        logger.error("Error creating category", it)
        throw it
    }

    override fun update(category: Category): Result<Unit, Throwable> = runCatching {
        categoryDatastoreOutputPort.update(category)
    }.onFailure {
        logger.error("Error updating category", it)
        throw it
    }

    override fun delete(storeId: UUID, categoryId: UUID): Result<Unit, Throwable> = runCatching {
        categoryDatastoreOutputPort.delete(storeId, categoryId)
    }.onFailure {
        logger.error("Error deleting category", it)
    }

    override fun patch(category: Category): Result<Unit, Throwable> = runCatching {
        categoryDatastoreOutputPort.patch(category)
    }.onFailure {
        logger.error("Error patching category", it)
    }
}