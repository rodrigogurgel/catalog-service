package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Item
import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateItemEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.PatchItemEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateItemEventDTO

fun CreateItemEventDTO.toDomain(): Item {
    return Item(
        storeId = storeId.toString().toUUID(),
        categoryId = categoryId.toString().toUUID(),
        itemId = itemId.toString().toUUID(),
        productId = productId.toString().toUUID(),
        price = price.toBigDecimal(),
        status = Status.AVAILABLE,
        index = index,
        reference = reference.toString()
    )
}

fun UpdateItemEventDTO.toDomain(): Item {
    return Item(
        storeId = storeId.toString().toUUID(),
        categoryId = null,
        itemId = itemId.toString().toUUID(),
        productId = productId.toString().toUUID(),
        price = price.toBigDecimal(),
        status = status.toDomain(),
        index = index,
    )
}

fun PatchItemEventDTO.toDomain(): Item {
    return Item(
        storeId = storeId.toString().toUUID(),
        categoryId = null,
        itemId = itemId.toString().toUUID(),
        productId = productId?.toString()?.toUUID(),
        price = price?.toBigDecimal(),
        status = status?.toDomain(),
        index = index,
    )
}
