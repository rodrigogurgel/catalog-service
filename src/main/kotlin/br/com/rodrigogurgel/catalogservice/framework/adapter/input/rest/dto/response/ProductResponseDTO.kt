package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response

import java.util.UUID

data class ProductResponseDTO(
    val id: UUID,
    val name: String,
    val description: String?,
    val imagePath: String?,
)
