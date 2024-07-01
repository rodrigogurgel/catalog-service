package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product

data class UpdateProductRequestDTO(
    val name: String,
    val description: String?,
    val image: String?,
)
