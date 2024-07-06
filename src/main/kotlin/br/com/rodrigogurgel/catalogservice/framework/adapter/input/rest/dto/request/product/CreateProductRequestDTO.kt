package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "Create Product payload")
data class CreateProductRequestDTO(
    @field:Schema(
        description = "the id of the product",
        required = false
    )
    val id: UUID = UUID.randomUUID(),
    @field:Schema(
        description = "the name of the product",
        example = "My Product",
        type = "string",
        minLength = Name.MIN_LENGTH,
        maxLength = Name.MAX_LENGTH,
        required = true
    )
    val name: String,
    @field:Schema(
        description = "the description of the product",
        example = "describe what the product have and something important about of the product",
        type = "string",
        minLength = Description.MIN_LENGTH,
        maxLength = Description.MAX_LENGTH,
        required = false
    )
    val description: String?,
    @field:Schema(
        description = "the URL image of the product",
        example = "https://picsum.photos/300/300",
        type = "string",
        required = false
    )
    val imagePath: String?,
)
