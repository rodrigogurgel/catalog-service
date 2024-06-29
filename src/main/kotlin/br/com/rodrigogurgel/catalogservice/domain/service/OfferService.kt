package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.exception.DuplicatedCustomizationException
import br.com.rodrigogurgel.catalogservice.domain.exception.DuplicatedOptionException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

object OfferService {
    fun getAllProducts(offer: Offer): List<Product> {
        return offer.customizations.flatMap { CustomizationService.getAllProducts(it) } + offer.product
    }

    private fun getDuplicatedCustomizationIds(offer: Offer): List<Id> {
        return offer.customizations.flatMap { customization -> customization.getCustomizationsInChildren() }
            .groupingBy { customization -> customization.id }
            .eachCount()
            .filter { idByCount -> idByCount.value > 1 }
            .map { duplicatedId -> duplicatedId.key }
    }

    private fun getDuplicatedOptionIds(offer: Offer): List<Id> {
        return offer.customizations.flatMap { option -> option.getOptionsInChildren() }
            .groupingBy { option -> option.id }
            .eachCount()
            .filter { idByCount -> idByCount.value > 1 }
            .map { duplicatedId -> duplicatedId.key }
    }

    fun validateDuplications(offer: Offer) {
        val duplicatedCustomizations = getDuplicatedCustomizationIds(offer)
        if (getDuplicatedCustomizationIds(offer).isNotEmpty()) {
            throw DuplicatedCustomizationException(
                duplicatedCustomizations
            )
        }
        val duplicatedOptionIds = getDuplicatedOptionIds(offer)
        if (duplicatedOptionIds.isNotEmpty()) throw DuplicatedOptionException(duplicatedOptionIds)
    }
}
