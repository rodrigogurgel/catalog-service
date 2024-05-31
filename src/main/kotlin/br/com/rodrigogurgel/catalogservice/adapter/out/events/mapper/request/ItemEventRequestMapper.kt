package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.request

import br.com.rodrigogurgel.catalogservice.domain.Item
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteItemEventRequestData

fun Item.toDeleteItemEventRequestData(): DeleteItemEventRequestData {
    return DeleteItemEventRequestData
        .newBuilder()
        .setStoreId(storeId!!.toString())
        .setItemId(itemId!!.toString())
        .build()
}
