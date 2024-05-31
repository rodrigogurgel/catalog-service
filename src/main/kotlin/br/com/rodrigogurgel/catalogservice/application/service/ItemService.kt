package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toCreateItemEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toDeleteItemEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toUpdateItemEventResponse
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.TransactionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ItemDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.events.CatalogEventOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Item
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.runCatching
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ItemService(
    private val itemDatastoreOutputPort: ItemDatastoreOutputPort,
    private val transactionInputPort: TransactionInputPort,
    private val catalogEventOutputPort: CatalogEventOutputPort,
    private val customizationInputPort: CustomizationInputPort,
    private val optionInputPort: OptionInputPort,
) : ItemInputPort {

    override suspend fun create(transaction: Transaction<Item>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            itemDatastoreOutputPort.create(transaction.data!!)
        }.andThen {
            catalogEventOutputPort.notifyResponse(it.toCreateItemEventResponse())
        }

    override suspend fun delete(transaction: Transaction<Item>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            itemDatastoreOutputPort.delete(
                transaction.data!!.storeId.toString().toUUID(),
                transaction.data!!.itemId.toString().toUUID()
            )
                .andThen { deleteCustomizationByReference(transaction) }
                .andThen { deleteOptionsByReference(transaction) }
        }.andThen { catalogEventOutputPort.notifyResponse(it.toDeleteItemEventResponse()) }

    override suspend fun update(transaction: Transaction<Item>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            itemDatastoreOutputPort.update(transaction.data!!)
        }.andThen { catalogEventOutputPort.notifyResponse(it.toUpdateItemEventResponse()) }

    override suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable> =
        itemDatastoreOutputPort.find(storeId, itemId)

    override suspend fun searchByProductId(storeId: UUID, productId: UUID): Result<List<Item>, Throwable> =
        itemDatastoreOutputPort.searchByProductId(storeId, productId)

    override suspend fun searchByCategoryId(storeId: UUID, categoryId: UUID): Result<List<Item>, Throwable> =
        itemDatastoreOutputPort.searchByCategoryId(storeId, categoryId)

    private suspend fun deleteCustomizationByReference(transaction: Transaction<Item>): Result<Unit, Throwable> =
        customizationInputPort.searchByReferenceBeginsWith(transaction.data!!.storeId!!, transaction.data!!.reference!!)
            .andThen { customizations ->
                runCatching {
                    customizations.forEach { customization ->
                        customizationInputPort.delete(
                            transaction.from(
                                customization,
                                TransactionType.DELETE_CUSTOMIZATION
                            ),
                            false
                        )
                    }
                }
            }

    private suspend fun deleteOptionsByReference(transaction: Transaction<Item>): Result<Unit, Throwable> =
        optionInputPort.searchByReferenceBeginsWith(transaction.data!!.storeId!!, transaction.data!!.reference!!)
            .andThen { options ->
                runCatching {
                    options.forEach { option ->
                        optionInputPort.delete(transaction.from(option, TransactionType.DELETE_OPTION), false)
                    }
                }
            }
}
