package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CreateOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.DeleteOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization.CustomizationRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization.UpdateCustomizationRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.CreateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.UpdateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.option.OptionRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.option.UpdateOptionRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.CustomizationResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.OfferResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.OptionResponseDTO
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
@RequestMapping("offers")
class OfferController(
    private val createOfferUseCase: CreateOfferUseCase,
    private val getOfferUseCase: GetOfferUseCase,
    private val deleteOfferUseCase: DeleteOfferUseCase,
    private val updateOfferUseCase: UpdateOfferUseCase,
    private val addCustomizationUseCase: AddCustomizationUseCase,
    private val updateCustomizationUseCase: UpdateCustomizationUseCase,
    private val removeCustomizationUseCase: RemoveCustomizationUseCase,
    private val addCustomizationOnChildrenUseCase: AddCustomizationOnChildrenUseCase,
    private val updateCustomizationOnChildrenUseCase: UpdateCustomizationOnChildrenUseCase,
    private val removeCustomizationOnChildrenUseCase: RemoveCustomizationOnChildrenUseCase,
    private val addOptionOnChildrenUseCase: AddOptionOnChildrenUseCase,
    private val updateOptionOnChildrenUseCase: UpdateOptionOnChildrenUseCase,
    private val removeOptionOnChildrenUseCase: RemoveOptionOnChildrenUseCase,
) {
    @Operation(summary = "Create an Offer in the Store", description = "Returns 201 if successful")
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
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun createOffer(
        @RequestParam storeId: UUID,
        @RequestBody createOfferRequestDTO: CreateOfferRequestDTO,
    ): OfferResponseDTO {
        val categoryId = createOfferRequestDTO.categoryId
        val offer = createOfferRequestDTO.toEntity()
        createOfferUseCase.execute(Id(storeId), Id(categoryId), offer)
        return offer.toResponseDTO()
    }

    @Operation(summary = "Get an Offer from the Store", description = "Returns 200 if successful")
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
        "/{offerId}",
        produces = [APPLICATION_JSON_VALUE]
    )
    fun getOfferById(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
    ): OfferResponseDTO {
        return getOfferUseCase.execute(Id(storeId), Id(offerId)).toResponseDTO()
    }

    @Operation(summary = "Delete an Offer from the Store", description = "Returns 204 if successful")
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
        "/{offerId}",
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteOffer(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
    ) {
        deleteOfferUseCase.execute(Id(storeId), Id(offerId))
    }

    @Operation(summary = "Update an Offer in the Store", description = "Returns 200 if successful")
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
        "/{offerId}",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun updateOffer(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @RequestBody updateOfferRequestDTO: UpdateOfferRequestDTO,
    ): OfferResponseDTO {
        val offer = updateOfferRequestDTO.toEntity(offerId)
        updateOfferUseCase.execute(Id(storeId), offer)
        return offer.toResponseDTO()
    }

    @Operation(
        summary = "Update an Offer in the Store adding a new Customization",
        description = "Returns 201 if successful"
    )
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
        "/{offerId}/customizations",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun addCustomization(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @RequestBody customizationRequestDTO: CustomizationRequestDTO,
    ): CustomizationResponseDTO {
        val customization = customizationRequestDTO.toEntity()
        addCustomizationUseCase.execute(Id(storeId), Id(offerId), customization)
        return customization.toResponseDTO()
    }

    @Operation(
        summary = "Update a Customization Offer",
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
    @PutMapping(
        "/{offerId}/customizations/{customizationId}",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun updateCustomization(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @PathVariable customizationId: UUID,
        @RequestBody updateCustomizationRequestDTO: UpdateCustomizationRequestDTO,
    ): CustomizationResponseDTO {
        val customization = updateCustomizationRequestDTO.toEntity(customizationId)
        updateCustomizationUseCase.execute(
            Id(storeId),
            Id(offerId),
            customization
        )
        return customization.toResponseDTO()
    }

    @Operation(
        summary = "Remove a Customization from the Offer",
        description = "Returns 204 if successful"
    )
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
        "/{offerId}/customizations/{customizationId}",
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeCustomization(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @PathVariable customizationId: UUID,
    ) {
        removeCustomizationUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(customizationId)
        )
    }

    @Operation(
        summary = "Create a Customization in a child of the Offer",
        description = "Returns 201 if successful"
    )
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
        "/{offerId}/options/{optionId}/customizations",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun addCustomizationOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @PathVariable optionId: UUID,
        @RequestBody customizationRequestDTO: CustomizationRequestDTO,
    ): CustomizationResponseDTO {
        val customization = customizationRequestDTO.toEntity()
        addCustomizationOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(optionId),
            customization
        )
        return customization.toResponseDTO()
    }

    @Operation(
        summary = "Update a Customization in a child of the Offer",
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
    @PutMapping(
        "/{offerId}/options/{optionId}/customizations/{customizationId}",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun updateCustomizationOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @PathVariable optionId: UUID,
        @PathVariable customizationId: UUID,
        @RequestBody updateCustomizationRequestDTO: UpdateCustomizationRequestDTO,
    ): CustomizationResponseDTO {
        val customization = updateCustomizationRequestDTO.toEntity(customizationId)
        updateCustomizationOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(optionId),
            customization
        )
        return customization.toResponseDTO()
    }

    @Operation(
        summary = "Delete a Customization in a child of the Offer",
        description = "Returns 204 if successful"
    )
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
        "/{offerId}/options/{optionId}/customizations/{customizationId}",
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeCustomizationOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @PathVariable optionId: UUID,
        @PathVariable customizationId: UUID,
    ) {
        removeCustomizationOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(optionId),
            Id(customizationId),
        )
    }

    @Operation(
        summary = "Create an Option in a child of the Offer",
        description = "Returns 201 if successful"
    )
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
        "/{offerId}/customizations/{customizationId}/options",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    fun addOptionOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @PathVariable customizationId: UUID,
        @RequestBody optionRequestDTO: OptionRequestDTO,
    ): OptionResponseDTO {
        val option = optionRequestDTO.toEntity()
        addOptionOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(customizationId),
            option
        )
        return option.toResponseDTO()
    }

    @Operation(
        summary = "Update an Option in a child of the Offer",
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
    @PutMapping(
        "/{offerId}/customizations/{customizationId}/options/{optionId}",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun updateOptionOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @PathVariable customizationId: UUID,
        @PathVariable optionId: UUID,
        @RequestBody updateOptionRequestDTO: UpdateOptionRequestDTO,
    ): OptionResponseDTO {
        val option = updateOptionRequestDTO.toEntity(optionId)
        updateOptionOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(customizationId),
            option
        )
        return option.toResponseDTO()
    }

    @Operation(
        summary = "Remove an Option in a child of the Offer",
        description = "Returns 204 if successful"
    )
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
        "/{offerId}/customizations/{customizationId}/options/{optionId}",
        produces = [APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeOptionOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable offerId: UUID,
        @PathVariable customizationId: UUID,
        @PathVariable optionId: UUID,
    ) {
        removeOptionOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(customizationId),
            Id(optionId)
        )
    }
}
