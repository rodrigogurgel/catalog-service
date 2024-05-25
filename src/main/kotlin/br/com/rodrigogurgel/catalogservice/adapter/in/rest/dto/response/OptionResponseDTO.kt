package br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.dto.response

import java.math.BigDecimal
import java.util.UUID

data class OptionResponseDTO (
    val optionId: UUID,
    val storeId: UUID,
    val customizationId: UUID,
    val product: ProductResponseDTO,
    val price: BigDecimal,
    val status: StatusResponseDTO,
    val index: Int,
    val customizations: List<CustomizationResponseDTO>,
)