package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.util.UUID

data class CreateCategoryRequestDTO(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String?,
    val status: Status,
)
