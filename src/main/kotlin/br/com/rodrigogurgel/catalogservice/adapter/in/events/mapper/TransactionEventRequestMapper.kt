package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionStatus
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import br.com.rodrigogurgel.catalogservice.`in`.events.request.TransactionEventRequest
import java.time.Instant
import java.util.UUID

fun <T> TransactionEventRequest.toDomain(
    storeId: UUID,
    type: TransactionType,
    data: T,
    status: TransactionStatus = TransactionStatus.CREATED,
    updatedAt: Instant = Instant.now(),
): Transaction<T> {
    return Transaction(
        transactionId = transactionId.toString().toUUID(),
        correlationId = correlationId.toString().toUUID(),
        storeId = storeId,
        type = type,
        status = status,
        message = null,
        data = data,
        createdBy = createdBy.toString(),
        createdFrom = createdFrom.toString(),
        createdAt = Instant.ofEpochMilli(createdAt),
        updatedAt = updatedAt,
    )
}
