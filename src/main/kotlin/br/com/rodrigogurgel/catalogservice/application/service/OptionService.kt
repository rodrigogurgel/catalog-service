package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toCreateOptionEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toDeleteOptionEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toUpdateOptionEventResponse
import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.TransactionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.OptionDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.events.CatalogEventOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.runCatching
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OptionService(
    private val optionDatastoreOutputPort: OptionDatastoreOutputPort,
    private val transactionInputPort: TransactionInputPort,
    private val catalogEventOutputPort: CatalogEventOutputPort,
    @Lazy private val customizationInputPort: CustomizationInputPort,
) : OptionInputPort {
    override suspend fun create(transaction: Transaction<Option>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            optionDatastoreOutputPort.create(transaction.data!!)
        }.andThen { catalogEventOutputPort.notifyResponse(it.toCreateOptionEventResponse()) }

    override suspend fun update(transaction: Transaction<Option>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            optionDatastoreOutputPort.update(transaction.data!!)
        }.andThen { catalogEventOutputPort.notifyResponse(it.toUpdateOptionEventResponse()) }

    override suspend fun delete(transaction: Transaction<Option>, cascade: Boolean): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            optionDatastoreOutputPort.delete(
                transaction.data!!.storeId.toString().toUUID(),
                transaction.data!!.optionId.toString().toUUID()
            )
                .andThen { if (cascade) deleteCustomizationsByReference(transaction) else Ok(Unit) }
                .andThen { if (cascade) deleteOptionsByReference(transaction) else Ok(Unit) }
        }.andThen { catalogEventOutputPort.notifyResponse(it.toDeleteOptionEventResponse()) }

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Option>, Throwable> = optionDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)

    override suspend fun searchByProductId(storeId: UUID, productId: UUID): Result<List<Option>, Throwable> =
        optionDatastoreOutputPort.searchByProductId(storeId, productId)

    private suspend fun deleteCustomizationsByReference(transaction: Transaction<Option>): Result<Unit, Throwable> =
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

    private suspend fun deleteOptionsByReference(transaction: Transaction<Option>): Result<Unit, Throwable> =
        searchByReferenceBeginsWith(transaction.data!!.storeId!!, transaction.data!!.reference!!)
            .andThen { options ->
                runCatching {
                    options.forEach { option ->
                        delete(transaction.from(option, TransactionType.DELETE_OPTION), false)
                    }
                }
            }
}
