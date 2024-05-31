package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response

import br.com.rodrigogurgel.catalogservice.domain.Item
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateItemEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateItemEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteItemEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteItemEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateItemEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateItemEventResponseData

fun Item.toCreateItemEventResponseData(): CreateItemEventResponseData {
    return CreateItemEventResponseData
        .newBuilder()
        .setItemId(itemId!!.toString())
        .setCategoryId(categoryId!!.toString())
        .setProductId(productId!!.toString())
        .setPrice(price!!.toDouble())
        .setStoreId(storeId!!.toString())
        .setStatus(status!!.toStatusEventResponse())
        .setReference(reference!!)
        .setIndex(index!!)
        .build()
}

fun Transaction<Item>.toCreateItemEventResponse(): CreateItemEventResponse {
    return CreateItemEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toCreateItemEventResponseData())
        .build()
}


fun Transaction<Item>.toUpdateItemEventResponse(): UpdateItemEventResponse {
    return UpdateItemEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toUpdateItemEventResponseData())
        .build()
}

fun Item.toUpdateItemEventResponseData(): UpdateItemEventResponseData {
    return UpdateItemEventResponseData
        .newBuilder()
        .setItemId(itemId!!.toString())
        .setProductId(productId!!.toString())
        .setPrice(price!!.toDouble())
        .setStoreId(storeId!!.toString())
        .setStatus(status!!.toStatusEventResponse())
        .setIndex(index!!)
        .build()
}

fun Transaction<Item>.toDeleteItemEventResponse(): DeleteItemEventResponse {
    return DeleteItemEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toDeleteItemEventResponseData())
        .build()
}

fun Item.toDeleteItemEventResponseData(): DeleteItemEventResponseData {
    return DeleteItemEventResponseData
        .newBuilder()
        .setItemId(itemId!!.toString())
        .setStoreId(storeId!!.toString())
        .build()
}