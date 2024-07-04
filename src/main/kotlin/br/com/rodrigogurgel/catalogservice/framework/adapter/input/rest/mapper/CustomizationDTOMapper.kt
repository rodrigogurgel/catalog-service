package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.CustomizationRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.CustomizationResponseDTO

fun CustomizationRequestDTO.toEntity(): Customization {
    return Customization(
        id = Id(id),
        name = Name(name),
        description = description?.let { Description(it) },
        quantity = Quantity(minPermitted, maxPermitted),
        status = status,
        options = options?.map { optionRequestDTO ->
            optionRequestDTO.toEntity()
        }.orEmpty().toMutableList()
    )
}

fun Customization.toResponseDTO(): CustomizationResponseDTO {
    return CustomizationResponseDTO(
        id = id.value,
        name = name.value,
        description = description?.value,
        status = status,
        minPermitted = quantity.minPermitted,
        maxPermitted = quantity.maxPermitted,
        options = options.map { option ->
            option.toResponseDTO()
        }
    )
}
