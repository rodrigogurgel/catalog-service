package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request

data class UpdateProductRequestDTO(
    val name: String,
    val description: String?,
    val image: String?,
)
