package br.com.rodrigogurgel.catalog.adapter.`in`.rest.dto.response

import java.util.UUID

data class CustomizationResponseDTO(
    val customizationId: UUID,
    val storeId: UUID,
    val name: String,
    val description: String?,
    val minPermitted: Int,
    val maxPermitted: Int,
    val status: StatusResponseDTO,
    val index: Int,
    val options: List<OptionResponseDTO>,
)