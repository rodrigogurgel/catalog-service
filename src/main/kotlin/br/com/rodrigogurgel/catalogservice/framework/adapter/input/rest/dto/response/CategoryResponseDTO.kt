package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.util.UUID

data class CategoryResponseDTO(
    val id: UUID,
    val name: String,
    val description: String?,
    val status: Status,
)
