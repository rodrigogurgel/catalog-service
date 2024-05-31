package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Item
import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateItemEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateItemEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteItemEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteItemEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateItemEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateItemEventRequestData

fun CreateItemEventRequest.toDomain(): Transaction<Item> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.CREATE_ITEM,
            data = this,
        )
    }
}

fun DeleteItemEventRequest.toDomain(): Transaction<Item> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.DELETE_ITEM,
            data = this,
        )
    }
}

fun UpdateItemEventRequest.toDomain(): Transaction<Item> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.UPDATE_ITEM,
            data = this,
        )
    }
}

fun CreateItemEventRequestData.toDomain(): Item {
    return Item(
        storeId = storeId.toString().toUUID(),
        categoryId = categoryId.toString().toUUID(),
        itemId = itemId.toString().toUUID(),
        productId = productId.toString().toUUID(),
        price = price.toBigDecimal(),
        status = Status.AVAILABLE,
        index = index,
        reference = reference.toString()
    )
}

fun UpdateItemEventRequestData.toDomain(): Item {
    return Item(
        storeId = storeId.toString().toUUID(),
        categoryId = null,
        itemId = itemId.toString().toUUID(),
        productId = productId.toString().toUUID(),
        price = price.toBigDecimal(),
        status = status.toDomain(),
        index = index,
    )
}

fun DeleteItemEventRequestData.toDomain(): Item {
    return Item(
        storeId = storeId.toString().toUUID(),
        categoryId = null,
        itemId = itemId.toString().toUUID(),
        productId = null,
        price = null,
        status = null,
        index = null,
    )
}
