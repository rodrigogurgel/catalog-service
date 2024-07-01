package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.category.CountCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CreateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.DeleteCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.UpdateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.CreateCategoryRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.UpdateCategoryRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.CategoryResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.PageResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toDomain
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/categories")
class CategoryController(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val countCategoriesUseCase: CountCategoriesUseCase,
) {
    @GetMapping
    fun getCategories(
        @RequestParam storeId: UUID,
        @RequestParam(defaultValue = "20", required = false) limit: Int,
        @RequestParam(defaultValue = "0", required = false) offset: Int,
        @RequestParam(required = false) beginsWith: String?,
    ): PageResponseDTO<CategoryResponseDTO> {
        val total = countCategoriesUseCase.execute(Id(storeId), beginsWith)
        val response =
            getCategoriesUseCase.execute(Id(storeId), limit, offset, beginsWith).map { category -> category.toDTO() }
        return PageResponseDTO(limit, offset, beginsWith, total, response)
    }

    @GetMapping("/{id}")
    fun getCategoryById(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") categoryId: UUID,
    ): CategoryResponseDTO {
        return getCategoryUseCase.execute(Id(storeId), Id(categoryId)).toDTO()
    }

    @PostMapping
    fun createCategory(
        @RequestParam storeId: UUID,
        @RequestBody createCategoryRequestDTO: CreateCategoryRequestDTO,
    ): CategoryResponseDTO {
        val category = createCategoryRequestDTO.toDomain()
        createCategoryUseCase.execute(Id(storeId), category)
        return category.toDTO()
    }

    @PutMapping("/{id}")
    fun updateCategory(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") categoryId: UUID,
        @RequestBody updateCategoryRequestDTO: UpdateCategoryRequestDTO,
    ) {
        val category = updateCategoryRequestDTO.toDomain(categoryId)
        updateCategoryUseCase.execute(Id(storeId), category)
    }

    @DeleteMapping("/{id}")
    fun deleteCategoryById(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") categoryId: UUID,
    ) {
        deleteCategoryUseCase.execute(Id(storeId), Id(categoryId))
    }
}
