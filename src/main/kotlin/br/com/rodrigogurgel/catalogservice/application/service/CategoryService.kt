package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toCreateCategoryEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toDeleteCategoryEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toUpdateCategoryEventResponse
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.TransactionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.events.CatalogEventOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.runCatching
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    private val transactionInputPort: TransactionInputPort,
    private val catalogEventOutputPort: CatalogEventOutputPort,
    private val itemInputPort: ItemInputPort,
) : CategoryInputPort {

    override suspend fun create(transaction: Transaction<Category>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            categoryDatastoreOutputPort.create(transaction.data!!)
        }.andThen { catalogEventOutputPort.notifyResponse(it.toCreateCategoryEventResponse()) }

    override suspend fun delete(transaction: Transaction<Category>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            categoryDatastoreOutputPort.delete(
                transaction.data!!.storeId!!,
                transaction.data!!.categoryId!!
            ).andThen { deleteItemCascade(transaction) }
        }.andThen { catalogEventOutputPort.notifyResponse(it.toDeleteCategoryEventResponse()) }

    override suspend fun update(transaction: Transaction<Category>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            categoryDatastoreOutputPort.update(transaction.data!!)
        }.andThen { catalogEventOutputPort.notifyResponse(it.toUpdateCategoryEventResponse()) }

    private suspend fun deleteItemCascade(transaction: Transaction<Category>): Result<Unit, Throwable> =
        itemInputPort.searchByCategoryId(transaction.data!!.storeId!!, transaction.data!!.categoryId!!)
            .andThen { items ->
                runCatching {
                    items.forEach { item ->
                        itemInputPort.delete(transaction.from(item, TransactionType.DELETE_ITEM))
                    }
                }
            }
}
