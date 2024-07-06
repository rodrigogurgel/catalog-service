package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.CreateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.UpdateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.OfferResponseDTO
import java.util.UUID

fun CreateOfferRequestDTO.toEntity(): Offer {
    return Offer(
        id = Id(id),
        name = Name(name),
        product = Product(
            id = Id(productId),
            name = Name("OFFER${id}HOLDER"),
            description = null,
            image = null
        ),
        price = Price(price),
        status = status,
        customizations = customizations?.map { customizationRequestDTO ->
            customizationRequestDTO.toEntity()
        }.orEmpty().toMutableList()
    )
}

fun Offer.toResponseDTO(): OfferResponseDTO {
    return OfferResponseDTO(
        id = id.value,
        name = name.value,
        productId = product.id.value,
        product = product.toResponseDTO(),
        price = price.value,
        status = status,
        customizations = customizations.map { customization ->
            customization.toResponseDTO()
        }
    )
}

fun UpdateOfferRequestDTO.toEntity(offerId: UUID): Offer {
    return Offer(
        id = Id(offerId),
        name = Name(name),
        product = Product(
            id = Id(productId),
            name = Name("OFFER${offerId}HOLDER"),
            description = null,
            image = null
        ),
        price = Price(price),
        status = status,
        customizations = customizations?.map { customizationRequestDTO ->
            customizationRequestDTO.toEntity()
        }.orEmpty().toMutableList()
    )
}
