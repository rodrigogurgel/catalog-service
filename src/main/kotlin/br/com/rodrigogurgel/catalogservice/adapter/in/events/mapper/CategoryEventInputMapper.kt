package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.domain.Category
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.CreateCategoryEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.PatchCategoryEventDTO
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.UpdateCategoryEventDTO

fun CreateCategoryEventDTO.toDomain(): Category {
    return Category(
        categoryId = categoryId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        index = index,
        status = status.toDomain(),
    )
}

fun UpdateCategoryEventDTO.toDomain(): Category {
    return Category(
        categoryId = categoryId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        index = index,
        status = status.toDomain(),
    )
}

fun PatchCategoryEventDTO.toDomain(): Category {
    return Category(
        categoryId = categoryId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name?.toString(),
        index = index,
        status = status?.toDomain(),
    )
}
