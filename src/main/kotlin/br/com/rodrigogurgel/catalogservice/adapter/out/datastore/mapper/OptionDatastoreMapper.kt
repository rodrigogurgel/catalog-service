package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.OptionDatastoreDTO
import br.com.rodrigogurgel.catalogservice.domain.Option

fun Option.toDatastoreDTO(): OptionDatastoreDTO {
    return OptionDatastoreDTO(
        optionId = optionId,
        storeId = storeId,
        productId = productId,
        price = price,
        status = status?.toDatastoreDTO(),
        index = index,
        customizationId = customizationId,
        reference = reference,
        maxPermitted = maxPermitted,
        minPermitted = minPermitted,
    )
}

fun OptionDatastoreDTO.toDomain(): Option {
    return Option(
        optionId = optionId,
        storeId = storeId,
        productId = productId,
        price = price,
        status = status?.toDomain(),
        index = index,
        customizationId = customizationId,
        reference = reference,
        maxPermitted = maxPermitted,
        minPermitted = minPermitted,
    )
}
