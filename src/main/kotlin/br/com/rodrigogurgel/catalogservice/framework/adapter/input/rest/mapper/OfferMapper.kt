package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.CreateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.UpdateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.OfferResponseDTO

fun CreateOfferRequestDTO.toEntity(): Offer {
    return run {
        Offer(
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
            }.orEmpty()
        )
    }
}

fun Offer.toResponseDTO(): OfferResponseDTO {
    return run {
        OfferResponseDTO(
            id = id.value,
            name = name.value,
            product = product.toResponseDTO(),
            price = price.normalizedValue(),
            status = status,
            customizations = getCustomizations().map { customization ->
                customization.toResponseDTO()
            }
        )
    }
}

fun UpdateOfferRequestDTO.toEntity(offerId: Id): Offer {
    return run {
        Offer(
            id = offerId,
            name = Name(name),
            product = Product(
                id = Id(productId),
                name = Name("OFFER${offerId.value}HOLDER"),
                description = null,
                image = null
            ),
            price = Price(price),
            status = status,
            customizations = customizations?.map { customizationRequestDTO ->
                customizationRequestDTO.toEntity()
            }.orEmpty()
        )
    }
}
