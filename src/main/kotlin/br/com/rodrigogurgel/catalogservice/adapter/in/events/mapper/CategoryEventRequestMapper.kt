package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateCategoryEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateCategoryEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteCategoryEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteCategoryEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateCategoryEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateCategoryEventRequestData

fun CreateCategoryEventRequest.toDomain(): Transaction<Category> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.CREATE_CATEGORY,
            data = this,
        )
    }
}

fun DeleteCategoryEventRequest.toDomain(): Transaction<Category> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.DELETE_CATEGORY,
            data = this,
        )
    }
}

fun UpdateCategoryEventRequest.toDomain(): Transaction<Category> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.UPDATE_CATEGORY,
            data = this,
        )
    }
}

fun CreateCategoryEventRequestData.toDomain(): Category {
    return Category(
        categoryId = categoryId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        index = index,
        status = status.toDomain(),
    )
}

fun UpdateCategoryEventRequestData.toDomain(): Category {
    return Category(
        categoryId = categoryId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        index = index,
        status = status.toDomain(),
    )
}

fun DeleteCategoryEventRequestData.toDomain(): Category {
    return Category(
        categoryId = categoryId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = null,
        index = null,
        status = null,
    )
}
