package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.CustomizationDatastoreDTO
import br.com.rodrigogurgel.catalogservice.domain.Customization

fun Customization.toDatastoreDTO(): CustomizationDatastoreDTO {
    return CustomizationDatastoreDTO(
        customizationId = customizationId,
        storeId = storeId,
        name = name,
        description = description,
        minPermitted = minPermitted,
        maxPermitted = maxPermitted,
        status = status?.toDatastoreDTO(),
        index = index,
        reference = reference,
    )
}

fun CustomizationDatastoreDTO.toDomain(): Customization {
    return Customization(
        customizationId = customizationId!!,
        storeId = storeId!!,
        name = name!!,
        description = description,
        minPermitted = minPermitted!!,
        maxPermitted = maxPermitted!!,
        status = status!!.toDomain(),
        index = index!!,
        reference = reference
    )
}
