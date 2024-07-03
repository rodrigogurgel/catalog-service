package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.math.BigDecimal
import java.util.UUID

data class OfferResponseDTO(
    val id: UUID,
    val name: String,
    val product: ProductResponseDTO,
    val price: BigDecimal,
    val status: Status,
    val customizations: List<CustomizationResponseDTO>,
)

data class CustomizationResponseDTO(
    val id: UUID,
    val name: String,
    val description: String?,
    val minPermitted: Int,
    val maxPermitted: Int,
    val status: Status,
    val options: List<OptionResponseDTO>,
)

data class OptionResponseDTO(
    val id: UUID,
    val product: ProductResponseDTO,
    val minPermitted: Int,
    val maxPermitted: Int,
    val price: BigDecimal,
    val status: Status,
    val customizations: List<CustomizationResponseDTO>,
)
