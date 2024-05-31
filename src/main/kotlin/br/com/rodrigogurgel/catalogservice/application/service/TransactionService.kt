package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.transaction.TransactionAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.TransactionInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.TransactionOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionStatus
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.mapBoth
import com.github.michaelbull.result.recover
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val transactionOutputPort: TransactionOutputPort,
) : TransactionInputPort {

    override suspend fun <T> runTransaction(
        transaction: Transaction<T>,
        shouldDiscard: suspend () -> Boolean,
        block: suspend () -> Result<Unit, Throwable>,
    ): Result<Transaction<T>, Throwable> = transactionOutputPort.create(transaction)
        .andThen {
            runBlocking {
                if (shouldDiscard()) {
                    Ok(transaction.copy(status = TransactionStatus.DISCARD, message = "Message discarded by condition"))
                } else {
                    block()
                }
            }.mapBoth({ Ok(transaction.copy(status = TransactionStatus.SUCCESS)) }, { error ->
                val result: Transaction<T> = when (error) {
                    is TransactionAlreadyExistsException -> transaction.copy(
                        status = TransactionStatus.DISCARD,
                        message = error.message
                    )

                    else -> transaction.copy(
                        status = TransactionStatus.FAILURE,
                        message = error.message
                    )
                }
                Ok(result)
            })
        }.andThen {
            transactionOutputPort.update(it)
        }.recover { error ->
            transaction.copy(
                status = TransactionStatus.FAILURE,
                message = error.message
            )
        }
}
