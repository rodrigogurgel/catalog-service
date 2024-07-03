package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.math.BigDecimal
import java.util.UUID

data class OptionRequestDTO(
    val id: UUID = UUID.randomUUID(),
    val productId: UUID,
    val minPermitted: Int,
    val maxPermitted: Int,
    val price: BigDecimal,
    val status: Status,
    val customizations: List<CustomizationRequestDTO>? = null,
)