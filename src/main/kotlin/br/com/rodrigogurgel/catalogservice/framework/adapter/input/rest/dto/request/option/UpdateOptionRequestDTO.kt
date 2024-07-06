package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.option

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization.CustomizationRequestDTO
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.util.UUID

@Schema(description = "Option payload")
data class UpdateOptionRequestDTO(
    @field:Schema(
        description = "the product id of the option",
        required = false
    )
    val productId: UUID,
    @field:Schema(
        description = "the minimum number of times that this option can be selected",
        example = "0",
        type = "int",
        minimum = "0",
    )
    val minPermitted: Int,
    @field:Schema(
        description = "the maximum number of times that this option can be selected",
        example = "1",
        type = "int",
        minimum = "1",
    )
    val maxPermitted: Int,
    @field:Schema(
        description = "the price of this option",
        example = "0",
        type = "double",
    )
    val price: BigDecimal,
    @field:Schema(
        description = "the status of the customization, can be AVAILABLE or UNAVAILABLE",
        type = "enum",
        required = true,
        implementation = Status::class
    )
    val status: Status,
    @field:Schema(
        description = "the customizations of the option",
        required = false
    )
    val customizations: List<CustomizationRequestDTO>? = null,
)
