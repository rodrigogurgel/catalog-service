package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response

import br.com.rodrigogurgel.catalogservice.domain.Product
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateProductEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateProductEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteProductEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteProductEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateProductEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateProductEventResponseData

fun Transaction<Product>.toCreateProductEventResponse(): CreateProductEventResponse {
    return CreateProductEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toCreateProductEventResponseData())
        .build()
}

fun Product.toCreateProductEventResponseData(): CreateProductEventResponseData {
    return CreateProductEventResponseData
        .newBuilder()
        .setProductId(productId!!.toString())
        .setStoreId(storeId!!.toString())
        .setName(name!!)
        .setDescription(description)
        .setImage(image)
        .build()
}

fun Transaction<Product>.toUpdateProductEventResponse(): UpdateProductEventResponse {
    return UpdateProductEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toUpdateProductEventResponseData())
        .build()
}

fun Product.toUpdateProductEventResponseData(): UpdateProductEventResponseData {
    return UpdateProductEventResponseData
        .newBuilder()
        .setProductId(productId!!.toString())
        .setStoreId(storeId!!.toString())
        .setName(name!!)
        .setDescription(description)
        .setImage(image)
        .build()
}

fun Transaction<Product>.toDeleteProductEventResponse(): DeleteProductEventResponse {
    return DeleteProductEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toDeleteProductEventResponseData())
        .build()
}

fun Product.toDeleteProductEventResponseData(): DeleteProductEventResponseData {
    return DeleteProductEventResponseData
        .newBuilder()
        .setProductId(productId!!.toString())
        .setStoreId(storeId!!.toString())
        .build()
}
