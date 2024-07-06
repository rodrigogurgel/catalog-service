package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Update Category payload")
data class UpdateCategoryRequestDTO(
    @field:Schema(
        description = "the name of the category",
        example = "My Category",
        type = "string",
        minLength = Name.MIN_LENGTH,
        maxLength = Name.MAX_LENGTH,
        required = false,
    )
    val name: String,
    @field:Schema(
        description = "the description of the category",
        example = "describe what the category have and something important about of the category",
        type = "string",
        minLength = Description.MIN_LENGTH,
        maxLength = Description.MAX_LENGTH,
        required = false
    )
    val description: String?,
    @field:Schema(
        description = "the status of the category, can be AVAILABLE or UNAVAILABLE",
        type = "enum",
        required = true,
        implementation = Status::class
    )
    val status: Status,
)
