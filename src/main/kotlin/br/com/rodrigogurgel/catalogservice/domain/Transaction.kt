package br.com.rodrigogurgel.catalogservice.domain

import java.time.Instant
import java.util.UUID

data class Transaction<T>(
    var transactionId: UUID?,
    var correlationId: UUID?,
    var storeId: UUID?,
    var type: TransactionType?,
    var status: TransactionStatus?,
    var message: String?,
    var data: T?,
    var createdBy: String?,
    var createdFrom: String?,
    var createdAt: Instant?,
    var updatedAt: Instant?,
) {
    fun <O> from(data: O, type: TransactionType): Transaction<O> {
        return Transaction(
            transactionId = UUID.randomUUID(),
            correlationId = correlationId,
            storeId = storeId,
            type = type,
            status = status,
            message = message,
            data = data,
            createdBy = createdBy,
            createdFrom = "SYSTEM",
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }
}
