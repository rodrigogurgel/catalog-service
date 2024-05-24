package br.com.rodrigogurgel.catalog.adapter.`in`.rest.dto.response

import java.math.BigDecimal
import java.util.UUID

data class ItemResponseDTO(
    val itemId: UUID,
    val storeId: UUID,
    val categoryId: UUID,
    val product: ProductResponseDTO,
    val price: BigDecimal,
    val status: StatusResponseDTO,
    val index: Int,
    val customizations: List<CustomizationResponseDTO>,
)