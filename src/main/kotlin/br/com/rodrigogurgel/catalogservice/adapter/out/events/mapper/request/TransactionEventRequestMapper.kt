package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.request

import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.`in`.events.request.TransactionEventRequest
import java.time.Instant
import java.util.UUID

fun Transaction<*>.toTransactionEventRequest() : TransactionEventRequest {
    return TransactionEventRequest
        .newBuilder()
        .setTransactionId(UUID.randomUUID().toString())
        .setCorrelationId(correlationId!!.toString())
        .setCreatedAt(Instant.now().toEpochMilli())
        .setCreatedBy("SYSTEM")
        .setCreatedFrom("SYSTEM")
        .build()
}