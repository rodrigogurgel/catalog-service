package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product

import java.util.UUID

data class CreateProductRequestDTO(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String?,
    val image: String?,
)
