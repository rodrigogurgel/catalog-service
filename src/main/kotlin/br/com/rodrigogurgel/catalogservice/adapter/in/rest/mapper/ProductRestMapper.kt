package br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.mapper

import br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.dto.response.ProductResponseDTO
import br.com.rodrigogurgel.catalogservice.domain.Product

fun Product.toResponseDTO(): ProductResponseDTO {
    return ProductResponseDTO(
        productId = productId!!,
        storeId = storeId!!,
        name = name!!,
        description = description,
        image = image,
    )
}
