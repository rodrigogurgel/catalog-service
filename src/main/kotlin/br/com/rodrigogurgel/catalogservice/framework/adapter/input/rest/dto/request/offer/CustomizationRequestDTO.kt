package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.util.UUID

data class CustomizationRequestDTO(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String?,
    val minPermitted: Int,
    val maxPermitted: Int,
    val status: Status,
    val options: List<OptionRequestDTO>? = null,
)
