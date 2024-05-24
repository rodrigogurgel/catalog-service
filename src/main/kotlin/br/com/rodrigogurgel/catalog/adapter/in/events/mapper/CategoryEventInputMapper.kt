package br.com.rodrigogurgel.catalog.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalog.application.common.toUUID
import br.com.rodrigogurgel.catalog.domain.Category
import br.com.rodrigogurgel.catalog.domain.Status
import br.com.rodrigogurgel.catalog.`in`.events.dto.CreateCategoryEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.PatchCategoryEventDTO
import br.com.rodrigogurgel.catalog.`in`.events.dto.UpdateCategoryEventDTO

fun CreateCategoryEventDTO.toDomain(): Category {
    return Category(
        categoryId = categoryId.toString().toUUID(),
        storeId = storeId.toString().toUUID(),
        name = name.toString(),
        index = index,
        status = Status.AVAILABLE,
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