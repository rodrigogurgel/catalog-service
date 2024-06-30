package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response

data class PageResponseDTO<T>(
    val limit: Int,
    val offset: Int,
    val beginsWith: String?,
    val total: Int,
    val data: List<T>,
)
