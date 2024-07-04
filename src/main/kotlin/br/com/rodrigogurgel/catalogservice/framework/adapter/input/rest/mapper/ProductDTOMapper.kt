package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Image
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product.CreateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product.UpdateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.ProductResponseDTO
import java.util.UUID

fun CreateProductRequestDTO.toEntity(): Product {
    return Product(
        id = Id(id),
        name = Name(name),
        description = description?.let { Description(it) },
        image = image?.let { Image(it) },
    )
}

fun Product.toResponseDTO(): ProductResponseDTO {
    return ProductResponseDTO(
        id = id.value,
        name = name.value,
        description = description?.value,
        image = image?.path,
    )
}

fun UpdateProductRequestDTO.toEntity(id: UUID): Product {
    return Product(
        id = Id(id),
        name = Name(name),
        description = description?.let { Description(it) },
        image = image?.let { Image(it) },
    )
}
