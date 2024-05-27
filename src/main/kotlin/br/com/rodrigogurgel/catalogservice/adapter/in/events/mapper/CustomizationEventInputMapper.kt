package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Customization
import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateCustomizationEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.PatchCustomizationEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateCustomizationEventDTO

fun CreateCustomizationEventDTO.toDomain(): Customization {
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

fun UpdateCustomizationEventDTO.toDomain(): Customization {
    return Customization(
        customizationId = customizationId.toString().toUUID(),
        storeId = customizationId.toString().toUUID(),
        name = name.toString(),
        description = description?.toString(),
        minPermitted = minPermitted,
        maxPermitted = maxPermitted,
        status = status.toDomain(),
        index = index,
    )
}

fun PatchCustomizationEventDTO.toDomain(): Customization {
    return Customization(
        customizationId = customizationId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name?.toString(),
        description = description?.toString(),
        minPermitted = minPermitted,
        maxPermitted = maxPermitted,
        status = status?.toDomain(),
        index = index,
    )
}