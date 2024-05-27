package br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.mapper

import br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.dto.response.CustomizationResponseDTO
import br.com.rodrigogurgel.catalogservice.domain.Customization
import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.domain.Product
import java.util.UUID

fun Customization.toResponseDTO(
    products: Map<UUID, Product>,
    customizations: Map<String, List<Customization>>,
    options: Map<String, List<Option>>,
): CustomizationResponseDTO {
    return CustomizationResponseDTO(
        customizationId = customizationId!!,
        storeId = storeId!!,
        name = name!!,
        description = description,
        minPermitted = minPermitted!!,
        maxPermitted = maxPermitted!!,
        status = status!!.toResponseDTO(),
        index = index!!,
        options = options[reference].orEmpty().map { option -> option.toResponseDTO(products, customizations, options) }
    )
}
