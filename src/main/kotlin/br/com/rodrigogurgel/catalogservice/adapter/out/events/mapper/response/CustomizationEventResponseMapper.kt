package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response

import br.com.rodrigogurgel.catalogservice.domain.Customization
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateCustomizationEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.CreateCustomizationEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteCustomizationEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.DeleteCustomizationEventResponseData
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateCustomizationEventResponse
import br.com.rodrigogurgel.catalogservice.out.events.response.UpdateCustomizationEventResponseData

fun Customization.toCreateCustomizationEventResponseData(): CreateCustomizationEventResponseData {
    return CreateCustomizationEventResponseData
        .newBuilder()
        .setCustomizationId(customizationId!!.toString())
        .setStoreId(storeId!!.toString())
        .setOptionId(optionId?.toString())
        .setName(name!!)
        .setStatus(status!!.toStatusEventResponse())
        .setMinPermitted(minPermitted)
        .setMaxPermitted(maxPermitted)
        .setDescription(description)
        .setReference(reference!!)
        .setIndex(index!!)
        .build()
}

fun Transaction<Customization>.toCreateCustomizationEventResponse(): CreateCustomizationEventResponse {
    return CreateCustomizationEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toCreateCustomizationEventResponseData())
        .build()
}


fun Transaction<Customization>.toUpdateCustomizationEventResponse(): UpdateCustomizationEventResponse {
    return UpdateCustomizationEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toUpdateCustomizationEventResponseData())
        .build()
}

fun Customization.toUpdateCustomizationEventResponseData(): UpdateCustomizationEventResponseData {
    return UpdateCustomizationEventResponseData
        .newBuilder()
        .setCustomizationId(customizationId!!.toString())
        .setStoreId(storeId!!.toString())
        .setName(name!!)
        .setStatus(status!!.toStatusEventResponse())
        .setMinPermitted(minPermitted)
        .setMaxPermitted(maxPermitted)
        .setDescription(description)
        .setIndex(index!!)
        .build()
}

fun Transaction<Customization>.toDeleteCustomizationEventResponse(): DeleteCustomizationEventResponse {
    return DeleteCustomizationEventResponse
        .newBuilder()
        .setTransaction(toTransactionEventResponse())
        .setData(data!!.toDeleteCustomizationEventResponseData())
        .build()
}

fun Customization.toDeleteCustomizationEventResponseData(): DeleteCustomizationEventResponseData {
    return DeleteCustomizationEventResponseData
        .newBuilder()
        .setCustomizationId(customizationId!!.toString())
        .setStoreId(storeId!!.toString())
        .build()
}