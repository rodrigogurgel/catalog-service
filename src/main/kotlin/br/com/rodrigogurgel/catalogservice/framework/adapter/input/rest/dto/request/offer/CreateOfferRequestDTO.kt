package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.math.BigDecimal
import java.util.UUID

data class CreateOfferRequestDTO(
    val id: UUID = UUID.randomUUID(),
    val categoryId: UUID,
    val name: String,
    val productId: UUID,
    val price: BigDecimal,
    val status: Status,
    val customizations: List<CustomizationRequestDTO>? = null,
)
