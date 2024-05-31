package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Customization
import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.domain.TransactionType
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateCustomizationEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.CreateCustomizationEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteCustomizationEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.DeleteCustomizationEventRequestData
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateCustomizationEventRequest
import br.com.rodrigogurgel.catalogservice.`in`.events.request.UpdateCustomizationEventRequestData
import java.util.UUID

fun CreateCustomizationEventRequest.toDomain(): Transaction<Customization> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.CREATE_CUSTOMIZATION,
            data = this,
        )
    }
}

fun DeleteCustomizationEventRequest.toDomain(): Transaction<Customization> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.DELETE_CUSTOMIZATION,
            data = this,
        )
    }
}

fun UpdateCustomizationEventRequest.toDomain(): Transaction<Customization> {
    return with(data.toDomain()) {
        transaction.toDomain(
            storeId = storeId!!,
            type = TransactionType.UPDATE_CUSTOMIZATION,
            data = this,
        )
    }
}

fun CreateCustomizationEventRequestData.toDomain(): Customization {
    return Customization(
        customizationId = customizationId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        optionId = optionId?.toString()?.toUUID(),
        name = name.toString(),
        description = description?.toString(),
        minPermitted = minPermitted,
        maxPermitted = maxPermitted,
        status = Status.AVAILABLE,
        index = index,
        reference = reference.toString()
    )
}

fun UpdateCustomizationEventRequestData.toDomain(): Customization {
    return Customization(
        customizationId = customizationId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        description = description?.toString(),
        minPermitted = minPermitted,
        maxPermitted = maxPermitted,
        status = status.toDomain(),
        index = index,
        optionId = UUID.randomUUID()
    )
}

fun DeleteCustomizationEventRequestData.toDomain(): Customization {
    return Customization(
        customizationId = customizationId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = null,
        description = null,
        minPermitted = null,
        maxPermitted = null,
        status = null,
        index = null,
    )
}
