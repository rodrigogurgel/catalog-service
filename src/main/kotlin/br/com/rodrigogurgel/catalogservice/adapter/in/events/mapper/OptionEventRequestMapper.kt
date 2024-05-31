package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateOptionEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateOptionEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteOptionEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteOptionEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateOptionEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateOptionEventRequestData

fun CreateOptionEventRequest.toDomain(): Transaction<Option> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.CREATE_OPTION,
            data = this,
        )
    }
}

fun DeleteOptionEventRequest.toDomain(): Transaction<Option> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.DELETE_OPTION,
            data = this,
        )
    }
}

fun UpdateOptionEventRequest.toDomain(): Transaction<Option> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.UPDATE_OPTION,
            data = this,
        )
    }
}

fun CreateOptionEventRequestData.toDomain(): Option {
    return Option(
        optionId = optionId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        customizationId = customizationId.toString().toUUID(),
        productId = productId.toString().toUUID(),
        price = price.toBigDecimal(),
        status = Status.AVAILABLE,
        minPermitted = minPermitted,
        maxPermitted = maxPermitted,
        index = index,
        reference = reference.toString()
    )
}

fun UpdateOptionEventRequestData.toDomain(): Option {
    return Option(
        optionId = optionId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        customizationId = null,
        productId = productId.toString().toUUID(),
        price = price.toBigDecimal(),
        status = status.toDomain(),
        minPermitted = minPermitted,
        maxPermitted = maxPermitted,
        index = index,
    )
}

fun DeleteOptionEventRequestData.toDomain(): Option {
    return Option(
        optionId = optionId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        customizationId = null,
        productId = null,
        price = null,
        status = null,
        minPermitted = null,
        maxPermitted = null,
        index = null,
    )
}
