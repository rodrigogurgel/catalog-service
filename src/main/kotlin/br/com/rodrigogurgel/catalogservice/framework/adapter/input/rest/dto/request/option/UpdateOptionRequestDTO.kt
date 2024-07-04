package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.option

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization.CustomizationRequestDTO
import java.math.BigDecimal
import java.util.UUID

data class UpdateOptionRequestDTO(
    val productId: UUID,
    val minPermitted: Int,
    val maxPermitted: Int,
    val price: BigDecimal,
    val status: Status,
    val customizations: List<CustomizationRequestDTO>? = null,
)
