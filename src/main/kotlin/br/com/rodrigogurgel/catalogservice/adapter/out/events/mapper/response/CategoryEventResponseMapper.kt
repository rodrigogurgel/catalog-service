package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response

import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateCategoryEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateCategoryEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteCategoryEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteCategoryEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateCategoryEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateCategoryEventResponseData

fun Category.toCreateCategoryEventResponseData(): CreateCategoryEventResponseData {
    return CreateCategoryEventResponseData
        .newBuilder()
        .setCategoryId(categoryId!!.toString())
        .setStoreId(storeId!!.toString())
        .setStatus(status!!.toStatusEventResponse())
        .setName(name)
        .setIndex(index!!)
        .build()
}

fun Transaction<Category>.toCreateCategoryEventResponse(): CreateCategoryEventResponse {
    return CreateCategoryEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toCreateCategoryEventResponseData())
        .build()
}

fun Transaction<Category>.toUpdateCategoryEventResponse(): UpdateCategoryEventResponse {
    return UpdateCategoryEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toUpdateCategoryEventResponseData())
        .build()
}

fun Category.toUpdateCategoryEventResponseData(): UpdateCategoryEventResponseData {
    return UpdateCategoryEventResponseData
        .newBuilder()
        .setCategoryId(categoryId!!.toString())
        .setStoreId(storeId!!.toString())
        .setStatus(status!!.toStatusEventResponse())
        .setName(name)
        .setIndex(index!!)
        .build()
}

fun Transaction<Category>.toDeleteCategoryEventResponse(): DeleteCategoryEventResponse {
    return DeleteCategoryEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toDeleteCategoryEventResponseData())
        .build()
}

fun Category.toDeleteCategoryEventResponseData(): DeleteCategoryEventResponseData {
    return DeleteCategoryEventResponseData
        .newBuilder()
        .setCategoryId(categoryId!!.toString())
        .setStoreId(storeId!!.toString())
        .build()
}
