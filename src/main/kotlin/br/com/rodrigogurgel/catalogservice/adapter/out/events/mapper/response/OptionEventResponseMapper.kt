package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response

import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateOptionEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateOptionEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteOptionEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteOptionEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateOptionEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateOptionEventResponseData

fun Option.toCreateOptionEventResponseData(): CreateOptionEventResponseData {
    return CreateOptionEventResponseData
        .newBuilder()
        .setOptionId(optionId!!.toString())
        .setStoreId(storeId!!.toString())
        .setCustomizationId(customizationId!!.toString())
        .setProductId(productId!!.toString())
        .setPrice(price!!.toDouble())
        .setMinPermitted(minPermitted)
        .setMaxPermitted(maxPermitted)
        .setStatus(status!!.toStatusEventResponse())
        .setIndex(index!!)
        .setReference(reference!!)
        .build()
}

fun Transaction<Option>.toCreateOptionEventResponse(): CreateOptionEventResponse {
    return CreateOptionEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toCreateOptionEventResponseData())
        .build()
}

fun Transaction<Option>.toUpdateOptionEventResponse(): UpdateOptionEventResponse {
    return UpdateOptionEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toUpdateOptionEventResponseData())
        .build()
}

fun Option.toUpdateOptionEventResponseData(): UpdateOptionEventResponseData {
    return UpdateOptionEventResponseData
        .newBuilder()
        .setOptionId(optionId!!.toString())
        .setStoreId(storeId!!.toString())
        .setProductId(productId!!.toString())
        .setPrice(price!!.toDouble())
        .setMinPermitted(minPermitted)
        .setMaxPermitted(maxPermitted)
        .setStatus(status!!.toStatusEventResponse())
        .setIndex(index!!)
        .build()
}

fun Transaction<Option>.toDeleteOptionEventResponse(): DeleteOptionEventResponse {
    return DeleteOptionEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toDeleteOptionEventResponseData())
        .build()
}

fun Option.toDeleteOptionEventResponseData(): DeleteOptionEventResponseData {
    return DeleteOptionEventResponseData
        .newBuilder()
        .setOptionId(optionId!!.toString())
        .setStoreId(storeId!!.toString())
        .build()
}
