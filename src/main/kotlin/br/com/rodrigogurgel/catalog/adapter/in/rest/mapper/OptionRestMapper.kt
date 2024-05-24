package br.com.rodrigogurgel.catalog.adapter.`in`.rest.mapper

import br.com.rodrigogurgel.catalog.adapter.`in`.rest.dto.response.OptionResponseDTO
import br.com.rodrigogurgel.catalog.domain.Customization
import br.com.rodrigogurgel.catalog.domain.Option
import br.com.rodrigogurgel.catalog.domain.Product
import java.util.UUID

fun Option.toResponseDTO(
    products: Map<UUID, Product>,
    customizations: Map<String, List<Customization>>,
    options: Map<String, List<Option>>,
): OptionResponseDTO {
    return OptionResponseDTO(
        optionId = optionId!!,
        storeId = storeId!!,
        customizationId = customizationId!!,
        product = products[productId]!!.toResponseDTO(),
        price = price!!,
        status = status!!.toResponseDTO(),
        index = index!!,
        customizations = customizations[reference].orEmpty().map { customization ->
            customization.toResponseDTO(
                products,
                customizations,
                options
            )
        },
    )
}