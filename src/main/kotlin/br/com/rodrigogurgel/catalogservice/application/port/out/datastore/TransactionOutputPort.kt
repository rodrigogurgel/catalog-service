package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Transaction
import com.github.michaelbull.result.Result
import java.util.UUID

interface TransactionOutputPort {
    suspend fun <T> create(transaction: Transaction<T>): Result<Transaction<T>, Throwable>
    suspend fun <T> update(transaction: Transaction<T>): Result<Transaction<T>, Throwable>
    suspend fun <T> search(transactionId: UUID): Result<Transaction<T>?, Throwable>
}
