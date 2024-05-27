package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Product
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateProductEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.PatchProductEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateProductEventDTO

fun CreateProductEventDTO.toDomain(): Product {
    return Product(
        productId = productId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        description = description?.toString(),
        image = image?.toString(),
    )
}

fun UpdateProductEventDTO.toDomain(): Product {
    return Product(
        productId = productId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        description = description?.toString(),
        image = image?.toString(),
    )
}

fun PatchProductEventDTO.toDomain(): Product {
    return Product(
        productId = productId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name?.toString(),
        description = description?.toString(),
        image = image?.toString(),
    )
}
