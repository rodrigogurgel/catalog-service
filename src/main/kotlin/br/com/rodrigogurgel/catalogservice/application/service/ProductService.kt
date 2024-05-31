package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toCreateProductEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toDeleteProductEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toUpdateProductEventResponse
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.TransactionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.events.CatalogEventOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Product
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.coroutines.runSuspendCatching
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductService(
    private val productDatastoreOutputPort: ProductDatastoreOutputPort,
    private val transactionInputPort: TransactionInputPort,
    private val catalogEventOutputPort: CatalogEventOutputPort,
    private val itemInputPort: ItemInputPort,
    private val optionInputPort: OptionInputPort,
) : ProductInputPort {

    override suspend fun create(transaction: Transaction<Product>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            productDatastoreOutputPort.create(transaction.data!!)
        }.andThen { catalogEventOutputPort.notifyResponse(it.toCreateProductEventResponse()) }

    override suspend fun update(transaction: Transaction<Product>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            productDatastoreOutputPort.update(transaction.data!!)
        }.andThen { catalogEventOutputPort.notifyResponse(it.toUpdateProductEventResponse()) }

    override suspend fun delete(transaction: Transaction<Product>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            productDatastoreOutputPort.delete(
                transaction.data!!.storeId.toString().toUUID(),
                transaction.data!!.productId.toString().toUUID()
            )
                .andThen { deleteItemCascade(transaction) }
                .andThen { deleteOptionCascade(transaction) }
        }.andThen { catalogEventOutputPort.notifyResponse(it.toDeleteProductEventResponse()) }

    override suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable> =
        productDatastoreOutputPort.find(storeId, productIds)

    private suspend fun deleteItemCascade(transaction: Transaction<Product>): Result<Unit, Throwable> =
        itemInputPort.searchByProductId(
            transaction.data!!.storeId.toString().toUUID(),
            transaction.data!!.productId.toString().toUUID()
        ).andThen { items ->
            runSuspendCatching {
                items.forEach { item ->
                    itemInputPort.delete(transaction.from(item, TransactionType.DELETE_ITEM))
                }
            }
        }

    private suspend fun deleteOptionCascade(transaction: Transaction<Product>): Result<Unit, Throwable> =
        optionInputPort.searchByProductId(
            transaction.data!!.storeId.toString().toUUID(),
            transaction.data!!.productId.toString().toUUID()
        ).andThen { options ->
            runSuspendCatching {
                options.forEach { option ->
                    optionInputPort.delete(transaction.from(option, TransactionType.DELETE_OPTION))
                }
            }
        }
}
