package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Product
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateProductEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateProductEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteProductEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteProductEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateProductEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateProductEventRequestData

fun CreateProductEventRequest.toDomain(): Transaction<Product> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.CREATE_PRODUCT,
            data = this,
        )
    }
}

fun DeleteProductEventRequest.toDomain(): Transaction<Product> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.DELETE_PRODUCT,
            data = this,
        )
    }
}

fun UpdateProductEventRequest.toDomain(): Transaction<Product> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.UPDATE_PRODUCT,
            data = this,
        )
    }
}

fun CreateProductEventRequestData.toDomain(): Product {
    return Product(
        productId = productId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        description = description?.toString(),
        image = image?.toString(),
    )
}

fun UpdateProductEventRequestData.toDomain(): Product {
    return Product(
        productId = productId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        description = description?.toString(),
        image = image?.toString(),
    )
}

fun DeleteProductEventRequestData.toDomain(): Product {
    return Product(
        productId = productId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = null,
        description = null,
        image = null,
    )
}
