package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateOptionEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.DeleteOptionEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.PatchOptionEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateOptionEventDTO

fun CreateOptionEventDTO.toDomain(): Option {
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

fun UpdateOptionEventDTO.toDomain(): Option {
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

fun PatchOptionEventDTO.toDomain(): Option {
    return Option(
        optionId = optionId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        customizationId = null,
        productId = productId?.toString()?.toUUID(),
        price = price?.toBigDecimal(),
        status = status?.toDomain(),
        minPermitted = minPermitted,
        maxPermitted = maxPermitted,
        index = index,
    )
}

fun DeleteOptionEventDTO.toDomain(): Option {
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
