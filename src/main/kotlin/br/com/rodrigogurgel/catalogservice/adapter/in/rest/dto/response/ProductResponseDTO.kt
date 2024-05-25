package br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.dto.response

import java.util.UUID

data class ProductResponseDTO(
    val productId: UUID,
    val storeId: UUID,
    val name: String,
    val description: String?,
    val image: String?
)