package br.com.rodrigogurgel.catalog.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalog.application.common.toUUID
import br.com.rodrigogurgel.catalog.domain.Item
import br.com.rodrigogurgel.catalog.domain.Status
import br.com.rodrigogurgel.catalog.`in`.events.dto.CreateItemEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.PatchItemEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.UpdateItemEventDTO
import java.util.UUID

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