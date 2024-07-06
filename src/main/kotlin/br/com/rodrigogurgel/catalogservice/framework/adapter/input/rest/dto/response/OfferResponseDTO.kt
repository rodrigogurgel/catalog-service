package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.math.BigDecimal
import java.util.UUID

data class OfferResponseDTO(
    val id: UUID,
    val name: String,
    val productId: UUID,
    val product: ProductResponseDTO,
    val price: BigDecimal,
    val status: Status,
    val customizations: List<CustomizationResponseDTO>,
)
