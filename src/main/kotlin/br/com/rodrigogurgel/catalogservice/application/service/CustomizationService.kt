package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toCreateCustomizationEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toDeleteCustomizationEventResponse
import br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response.toUpdateCustomizationEventResponse
import br.com.rodrigogurgel.catalogservice.application.port.`in`.CustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.OptionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.TransactionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.CustomizationDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.events.CatalogEventOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Customization
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
class CustomizationService(
    private val customizationDatastoreOutputPort: CustomizationDatastoreOutputPort,
    private val transactionInputPort: TransactionInputPort,
    private val catalogEventOutputPort: CatalogEventOutputPort,
    @Lazy private val optionInputPort: OptionInputPort,
) : CustomizationInputPort {
    override suspend fun create(transaction: Transaction<Customization>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            customizationDatastoreOutputPort.create(transaction.data!!)
        }.andThen {
            catalogEventOutputPort.notifyResponse(it.toCreateCustomizationEventResponse())
        }

    override suspend fun delete(transaction: Transaction<Customization>, cascade: Boolean): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            customizationDatastoreOutputPort.delete(
                transaction.data!!.storeId!!,
                transaction.data!!.customizationId!!
            )
                .andThen { if (cascade) deleteOptionsByReference(transaction) else Ok(Unit) }
                .andThen { if (cascade) deleteCustomizationsByReference(transaction) else Ok(Unit) }
        }.andThen { catalogEventOutputPort.notifyResponse(it.toDeleteCustomizationEventResponse()) }

    override suspend fun update(transaction: Transaction<Customization>): Result<Unit, Throwable> =
        transactionInputPort.runTransaction(transaction, { false }) {
            customizationDatastoreOutputPort.update(transaction.data!!)
        }.andThen { catalogEventOutputPort.notifyResponse(it.toUpdateCustomizationEventResponse()) }

    override suspend fun searchByReferenceBeginsWith(
        storeId: UUID,
        reference: String,
    ): Result<List<Customization>, Throwable> =
        customizationDatastoreOutputPort.searchByReferenceBeginsWith(storeId, reference)

    private suspend fun deleteOptionsByReference(transaction: Transaction<Customization>): Result<Unit, Throwable> =
        optionInputPort.searchByReferenceBeginsWith(transaction.data!!.storeId!!, transaction.data!!.reference!!)
            .andThen { options ->
                runCatching {
                    options.forEach { option ->
                        optionInputPort.delete(transaction.from(option, TransactionType.DELETE_OPTION), false)
                    }
                }
            }

    private suspend fun deleteCustomizationsByReference(
        transaction: Transaction<Customization>,
    ): Result<Unit, Throwable> =
        searchByReferenceBeginsWith(transaction.data!!.storeId!!, transaction.data!!.reference!!)
            .andThen { customizations ->
                runCatching {
                    customizations.forEach { customization ->
                        delete(transaction.from(customization, TransactionType.DELETE_CUSTOMIZATION), false)
                    }
                }
            }
}
