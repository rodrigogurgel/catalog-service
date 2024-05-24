package br.com.rodrigogurgel.catalog.adapter.`in`.rest.dto.response

import java.util.UUID

data class CategoryResponseDTO(
    val categoryId: UUID,
    val storeId: UUID,
    val name: String,
    val status: StatusResponseDTO,
    val index: Int,
    val reference: String,
)
