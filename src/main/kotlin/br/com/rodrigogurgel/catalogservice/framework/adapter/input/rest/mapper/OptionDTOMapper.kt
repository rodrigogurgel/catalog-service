package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.OptionRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.OptionResponseDTO

fun OptionRequestDTO.toEntity(): Option {
    return Option(
        id = Id(id),
        product = Product(
            id = Id(productId),
            name = Name("OPTION${id}HOLDER"),
            description = null,
            image = null
        ),
        price = Price(price),
        quantity = Quantity(minPermitted, maxPermitted),
        status = status,
        customizations = customizations?.map { optionRequestDTO ->
            optionRequestDTO.toEntity()
        }.orEmpty().toMutableList()
    )
}

fun Option.toResponseDTO(): OptionResponseDTO {
    return OptionResponseDTO(
        id = id.value,
        product = product.toResponseDTO(),
        price = price.normalizedValue(),
        status = status,
        minPermitted = quantity.minPermitted,
        maxPermitted = quantity.maxPermitted,
        customizations = customizations.map { customization ->
            customization.toResponseDTO()
        }
    )
}
