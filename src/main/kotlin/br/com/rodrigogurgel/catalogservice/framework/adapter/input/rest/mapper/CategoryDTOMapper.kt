package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.CreateCategoryRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.UpdateCategoryRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.CategoryResponseDTO
import java.util.UUID

fun CreateCategoryRequestDTO.toEntity(): Category {
    return Category(
        id = Id(id),
        name = Name(name),
        description = description?.let { Description(it) },
        status = status
    )
}

fun Category.toResponseDTO(): CategoryResponseDTO {
    return CategoryResponseDTO(
        id = id.value,
        name = name.value,
        description = description?.value,
        status = status
    )
}

fun UpdateCategoryRequestDTO.toEntity(categoryId: UUID): Category {
    return Category(
        id = Id(categoryId),
        name = Name(name),
        description = description?.let { Description(it) },
        status = status
    )
}
