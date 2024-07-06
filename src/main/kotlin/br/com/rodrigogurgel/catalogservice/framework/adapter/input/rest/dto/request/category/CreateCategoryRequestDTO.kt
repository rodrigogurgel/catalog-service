package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "Create Category payload")
data class CreateCategoryRequestDTO(
    @field:Schema(
        description = "the id of the category",
        required = false
    )
    val id: UUID = UUID.randomUUID(),
    @field:Schema(
        description = "the name of the category",
        example = "My Category",
        type = "string",
        minLength = Name.MIN_LENGTH,
        maxLength = Name.MAX_LENGTH,
        required = true
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
