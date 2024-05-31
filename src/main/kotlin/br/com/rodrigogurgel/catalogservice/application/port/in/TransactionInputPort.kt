package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Transaction
import com.github.michaelbull.result.Result

interface TransactionInputPort {
    suspend fun <T> runTransaction(
        transaction: Transaction<T>,
        shouldDiscard: suspend () -> Boolean,
        block: suspend () -> Result<Unit, Throwable>,
    ): Result<Transaction<T>, Throwable>
}
