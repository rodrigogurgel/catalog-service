package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CreateOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.DeleteOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.CreateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.UpdateOfferRequestDTO
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
}
