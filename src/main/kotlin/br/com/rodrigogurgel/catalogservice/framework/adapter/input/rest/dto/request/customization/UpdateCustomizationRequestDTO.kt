package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.option.OptionRequestDTO
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Update Customization payload")
data class UpdateCustomizationRequestDTO(
    @field:Schema(
        description = "the name of the customization",
        example = "My Customization",
        type = "string",
        minLength = Name.MIN_LENGTH,
        maxLength = Name.MAX_LENGTH,
        required = true
    )
    val name: String,
    @field:Schema(
        description = "the description of the customization",
        example = "describe what the customization have and something important about of the customization",
        type = "string",
        minLength = Description.MIN_LENGTH,
        maxLength = Description.MAX_LENGTH,
        required = false
    )
    val description: String?,
    @field:Schema(
        description = "the minimum number of options to be selected from this customization",
        example = "0",
        type = "int",
        minimum = "0",
    )
    val minPermitted: Int,
    @field:Schema(
        description = "the maximum number of options to be selected from this customization, " +
            "this value needs to be greater or equal than the minimum permitted",
        example = "1",
        type = "int",
        minimum = "1",
    )
    val maxPermitted: Int,
    @field:Schema(
        description = "the status of the customization, can be AVAILABLE or UNAVAILABLE",
        type = "enum",
        required = true,
        implementation = Status::class
    )
    val status: Status,
    @field:Schema(
        description = "the options for the customization, the options list needs have at least one available option " +
            "and the size of available option needs be greater than the minimum permitted of the customization",
        type = "array",
        required = true,
        minLength = 1
    )
    val options: List<OptionRequestDTO>,
)
