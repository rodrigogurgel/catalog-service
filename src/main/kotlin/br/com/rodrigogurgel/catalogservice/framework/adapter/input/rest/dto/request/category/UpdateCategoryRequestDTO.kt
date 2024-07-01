package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category

import br.com.rodrigogurgel.catalogservice.domain.vo.Status

data class UpdateCategoryRequestDTO(
    val name: String,
    val description: String?,
    val status: Status,
)
