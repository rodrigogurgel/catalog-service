package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer

import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization.CustomizationRequestDTO
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.UUID

@Schema(description = "Update Offer payload")
data class UpdateOfferRequestDTO(
    @field:Schema(
        description = "the parent category id of the offer",
        required = false
    )
    val categoryId: UUID,
    @field:Schema(
        description = "the name of the offer",
        example = "My Offer",
        type = "string",
        minLength = Name.MIN_LENGTH,
        maxLength = Name.MAX_LENGTH,
        required = true
    )
    val name: String,
    @field:Schema(
        description = "the product id of the offer",
        required = false
    )
    val productId: UUID,
    @field:Schema(
        description = "the price of this offer, the price needs be greater than zero",
        example = "0.01",
        type = "double",
    )
    val price: BigDecimal,
    @field:Schema(
        description = "the status of the category, can be AVAILABLE or UNAVAILABLE",
        type = "enum",
        required = true,
        implementation = Status::class
    )
    val status: Status,
    @field:Schema(
        description = "the customizations of the offer",
        required = false
    )
    val customizations: List<CustomizationRequestDTO>? = null,
)
