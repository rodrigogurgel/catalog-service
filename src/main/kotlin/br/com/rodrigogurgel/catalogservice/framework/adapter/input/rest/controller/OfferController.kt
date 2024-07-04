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
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.OfferResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toResponseDTO
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
    @PostMapping
    fun createOffer(
        @RequestParam storeId: UUID,
        @RequestBody createOfferRequestDTO: CreateOfferRequestDTO,
    ): OfferResponseDTO {
        val categoryId = createOfferRequestDTO.categoryId
        val offer = createOfferRequestDTO.toEntity()
        createOfferUseCase.execute(Id(storeId), Id(categoryId), offer)
        return offer.toResponseDTO()
    }

    @GetMapping("/{id}")
    fun getOfferById(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
    ): OfferResponseDTO {
        return getOfferUseCase.execute(Id(storeId), Id(offerId)).toResponseDTO()
    }

    @DeleteMapping("/{id}")
    fun deleteOffer(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
    ) {
        deleteOfferUseCase.execute(Id(storeId), Id(offerId))
    }

    @PutMapping("/{id}")
    fun updateOffer(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
        @RequestBody updateOfferRequestDTO: UpdateOfferRequestDTO,
    ) {
        updateOfferUseCase.execute(Id(storeId), updateOfferRequestDTO.toEntity(Id(offerId)))
    }

    @PostMapping("/{id}/customizations")
    fun addCustomization(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
        @RequestBody customizationRequestDTO: CustomizationRequestDTO,
    ) {
        addCustomizationUseCase.execute(Id(storeId), Id(offerId), customizationRequestDTO.toEntity())
    }

    @PutMapping("/{id}/customizations/{customizationId}")
    fun updateCustomization(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
        @PathVariable customizationId: UUID,
        @RequestBody updateCustomizationRequestDTO: UpdateCustomizationRequestDTO,
    ) {
        updateCustomizationUseCase.execute(
            Id(storeId),
            Id(offerId),
            updateCustomizationRequestDTO.toEntity(customizationId)
        )
    }

    @DeleteMapping("/{id}/customizations/{customizationId}")
    fun removeCustomization(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
        @PathVariable customizationId: UUID,
    ) {
        removeCustomizationUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(customizationId)
        )
    }

    @PostMapping("/{id}/options/{optionId}/customizations")
    fun addCustomizationOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
        @PathVariable optionId: UUID,
        @RequestBody customizationRequestDTO: CustomizationRequestDTO,
    ) {
        addCustomizationOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(optionId),
            customizationRequestDTO.toEntity()
        )
    }

    @PutMapping("/{id}/options/{optionId}/customizations/{customizationId}")
    fun updateCustomizationOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
        @PathVariable optionId: UUID,
        @PathVariable customizationId: UUID,
        @RequestBody updateCustomizationRequestDTO: UpdateCustomizationRequestDTO,
    ) {
        updateCustomizationOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(optionId),
            updateCustomizationRequestDTO.toEntity(customizationId)
        )
    }

    @DeleteMapping("/{id}/options/{optionId}/customizations/{customizationId}")
    fun removeCustomizationOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
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

    @PostMapping("/{id}/customizations/{customizationId}/options")
    fun addOptionOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
        @PathVariable customizationId: UUID,
        @RequestBody optionRequestDTO: OptionRequestDTO,
    ) {
        addOptionOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(customizationId),
            optionRequestDTO.toEntity()
        )
    }

    @PutMapping("/{id}/customizations/{customizationId}/options/{optionId}")
    fun updateOptionOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
        @PathVariable customizationId: UUID,
        @PathVariable optionId: UUID,
        @RequestBody updateOptionRequestDTO: UpdateOptionRequestDTO,
    ) {
        updateOptionOnChildrenUseCase.execute(
            Id(storeId),
            Id(offerId),
            Id(customizationId),
            updateOptionRequestDTO.toEntity(optionId)
        )
    }

    @DeleteMapping("/{id}/customizations/{customizationId}/options/{optionId}")
    fun removeOptionOnChildren(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") offerId: UUID,
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
