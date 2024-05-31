package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.request

import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteOptionEventRequestData

fun Option.toDeleteOptionEventRequestData(): DeleteOptionEventRequestData {
    return DeleteOptionEventRequestData
        .newBuilder()
        .setStoreId(storeId!!.toString())
        .setOptionId(optionId!!.toString())
        .build()
}