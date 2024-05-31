package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response

import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionStatus
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import br.com.rodrigogurgel.catalogservice.out.events.response.TransactionEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.TransactionStatusEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.TransactionTypeEventResponse

fun Transaction<*>.toTransactionEventResponse(): TransactionEventResponse {
    return TransactionEventResponse
        .newBuilder()
        .setTransactionId(transactionId!!.toString())
        .setCorrelationId(correlationId!!.toString())
        .setStoreId(storeId!!.toString())
        .setType(type!!.toTransactionTypeEventResponse())
        .setStatus(status!!.toTransactionStatusEventResponse())
        .setMessage(message)
        .setCreatedBy(createdBy)
        .setCreatedFrom(createdFrom)
        .setCreatedAt(createdAt!!.toEpochMilli())
        .setUpdatedAt(updatedAt!!.toEpochMilli())
        .build()
}

fun TransactionStatus.toTransactionStatusEventResponse(): TransactionStatusEventResponse {
    return when (this) {
        TransactionStatus.CREATED -> TransactionStatusEventResponse.CREATED
        TransactionStatus.DISCARD -> TransactionStatusEventResponse.DISCARD
        TransactionStatus.SUCCESS -> TransactionStatusEventResponse.SUCCESS
        TransactionStatus.FAILURE -> TransactionStatusEventResponse.FAILURE
    }
}

fun TransactionType.toTransactionTypeEventResponse(): TransactionTypeEventResponse {
    return when (this) {
        TransactionType.CREATE_CATEGORY -> TransactionTypeEventResponse.CREATE_CATEGORY
        TransactionType.DELETE_CATEGORY -> TransactionTypeEventResponse.DELETE_CATEGORY
        TransactionType.UPDATE_CATEGORY -> TransactionTypeEventResponse.UPDATE_CATEGORY
        TransactionType.CREATE_ITEM -> TransactionTypeEventResponse.CREATE_ITEM
        TransactionType.DELETE_ITEM -> TransactionTypeEventResponse.DELETE_ITEM
        TransactionType.UPDATE_ITEM -> TransactionTypeEventResponse.UPDATE_ITEM
        TransactionType.CREATE_PRODUCT -> TransactionTypeEventResponse.CREATE_PRODUCT
        TransactionType.DELETE_PRODUCT -> TransactionTypeEventResponse.DELETE_PRODUCT
        TransactionType.UPDATE_PRODUCT -> TransactionTypeEventResponse.UPDATE_PRODUCT
        TransactionType.CREATE_CUSTOMIZATION -> TransactionTypeEventResponse.CREATE_CUSTOMIZATION
        TransactionType.DELETE_CUSTOMIZATION -> TransactionTypeEventResponse.DELETE_CUSTOMIZATION
        TransactionType.UPDATE_CUSTOMIZATION -> TransactionTypeEventResponse.UPDATE_CUSTOMIZATION
        TransactionType.CREATE_OPTION -> TransactionTypeEventResponse.CREATE_OPTION
        TransactionType.DELETE_OPTION -> TransactionTypeEventResponse.DELETE_OPTION
        TransactionType.UPDATE_OPTION -> TransactionTypeEventResponse.UPDATE_OPTION
    }
}
