package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.category.CountCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CreateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.DeleteCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.UpdateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CountOffersUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOffersUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.CreateCategoryRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.UpdateCategoryRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.CategoryResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.OfferResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.PageResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toResponseDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
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
    private val countOffersUseCase: CountOffersUseCase,
    private val getOffersUseCase: GetOffersUseCase,
) {
    @Operation(summary = "Get a page of Categories of the Store", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful Operation",
                useReturnTypeSchema = true,
            ),
        ]
    )
    @GetMapping(
        produces = [APPLICATION_JSON_VALUE]
    )
    fun getCategories(
        @RequestParam storeId: UUID,
        @RequestParam(defaultValue = "20", required = false) limit: Int,
        @RequestParam(defaultValue = "0", required = false) offset: Int,
        @RequestParam(required = false) beginsWith: String?,
    ): PageResponseDTO<CategoryResponseDTO> {
        val total = countCategoriesUseCase.execute(Id(storeId), beginsWith)
        val response =
            getCategoriesUseCase.execute(
                Id(storeId),
                limit,
                offset,
                beginsWith
            ).map { category -> category.toResponseDTO() }
        return PageResponseDTO(limit, offset, beginsWith, total, response)
    }

    @Operation(summary = "Get a Category of the Store", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful Operation",
                useReturnTypeSchema = true,
            ),
        ]
    )
    @GetMapping(
        "/{categoryId}",
        consumes = [APPLICATION_JSON_VALUE]
    )
    fun getCategoryById(
        @RequestParam storeId: UUID,
        @PathVariable categoryId: UUID,
    ): CategoryResponseDTO {
        return getCategoryUseCase.execute(Id(storeId), Id(categoryId)).toResponseDTO()
    }

    @Operation(summary = "Create a Category in the Store", description = "Returns 201 if successful")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Successful Operation",
                useReturnTypeSchema = true,
            ),
        ]
    )
    @PostMapping(
        produces = [APPLICATION_JSON_VALUE],
        consumes = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(
        @RequestParam storeId: UUID,
        @RequestBody createCategoryRequestDTO: CreateCategoryRequestDTO,
    ): CategoryResponseDTO {
        val category = createCategoryRequestDTO.toEntity()
        createCategoryUseCase.execute(Id(storeId), category)
        return category.toResponseDTO()
    }

    @Operation(summary = "Update a Category in the Store", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful Operation",
                useReturnTypeSchema = true,
            ),
        ]
    )
    @PutMapping(
        "/{categoryId}",
        produces = [APPLICATION_JSON_VALUE],
        consumes = [APPLICATION_JSON_VALUE]
    )
    fun updateCategory(
        @RequestParam storeId: UUID,
        @PathVariable categoryId: UUID,
        @RequestBody updateCategoryRequestDTO: UpdateCategoryRequestDTO,
    ): CategoryResponseDTO {
        val category = updateCategoryRequestDTO.toEntity(categoryId)
        updateCategoryUseCase.execute(Id(storeId), category)
        return category.toResponseDTO()
    }

    @Operation(summary = "Delete a Category in the Store", description = "Returns 204 if successful")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Successful Operation",
                useReturnTypeSchema = true,
            ),
        ]
    )
    @DeleteMapping(
        "/{categoryId}",
        consumes = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCategoryById(
        @RequestParam storeId: UUID,
        @PathVariable categoryId: UUID,
    ) {
        deleteCategoryUseCase.execute(Id(storeId), Id(categoryId))
    }

    @Operation(
        summary = "Get a page of Offers from the Category in the Store",
        description = "Returns 200 if successful"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful Operation",
                useReturnTypeSchema = true,
            ),
        ]
    )
    @GetMapping(
        "/{categoryId}/offers",
        produces = [APPLICATION_JSON_VALUE]
    )
    fun getOffers(
        @PathVariable categoryId: UUID,
        @RequestParam storeId: UUID,
        @RequestParam(defaultValue = "20", required = false) limit: Int,
        @RequestParam(defaultValue = "0", required = false) offset: Int,
        @RequestParam(required = false) beginsWith: String?,
    ): PageResponseDTO<OfferResponseDTO> {
        val total = countOffersUseCase.execute(Id(storeId), Id(categoryId), beginsWith)
        val offers = getOffersUseCase.execute(Id(storeId), Id(categoryId), limit, offset, beginsWith)

        return PageResponseDTO(limit, offset, beginsWith, total, offers.map { offer -> offer.toResponseDTO() })
    }
}
