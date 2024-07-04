package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.math.BigDecimal
import java.util.UUID

data class OptionResponseDTO(
    val id: UUID,
    val productId: UUID,
    val product: ProductResponseDTO,
    val minPermitted: Int,
    val maxPermitted: Int,
    val price: BigDecimal,
    val status: Status,
    val customizations: List<CustomizationResponseDTO>,
)
