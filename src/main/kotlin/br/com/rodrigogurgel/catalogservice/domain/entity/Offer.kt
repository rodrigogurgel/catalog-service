package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.exception.OfferPriceZeroException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Status

class Offer(
    val id: Id,
    var name: Name,
    var product: Product,
    price: Price,
    var status: Status,
    val customizations: MutableList<Customization>,
) {
    var price: Price = price
        set(value) {
            field = value
            validatePrice()
        }

    init {
        validatePrice()
    }

    private val customizationsById: Map<Id, Customization>
        get() = (
            customizations + customizations
                .flatMap { customization -> customization.getAllCustomizationsInChildren() }
            )
            .associateBy { it.id }
    private val optionsById: Map<Id, Option>
        get() = customizations
            .flatMap { it.getAllOptionsInChildren() }
            .associateBy { it.id }

    private fun validatePrice() {
        if (price.normalizedValue() <= Price.ZERO.normalizedValue()) throw OfferPriceZeroException(id)
    }

    /**
     * Adds a customization to the Offer.
     *
     * @param customization the customization to be added
     * @throws CustomizationAlreadyExistsException if a customization with the same id already exists in the Offer
     */
    fun addCustomization(customization: Customization) {
        if (customizations.any { it.id == customization.id }) throw CustomizationAlreadyExistsException(id)
        customizations.add(customization)
    }

    /**
     * Updates a customization in the Offer.
     *
     * @param customization the customization to be updated
     * @throws CustomizationNotFoundException if the customization with the specified id is not found in the Offer
     */
    fun updateCustomization(customization: Customization) {
        val index = customizations.indexOfFirst { it.id == customization.id }
        if (index == -1) throw CustomizationNotFoundException(customization.id)
        customizations[index] = customization
    }

    /**
     * Removes a customization from the Offer.
     *
     * @param customizationId the ID of the customization to be removed
     */
    fun removeCustomization(customizationId: Id) {
        customizations.removeIf { it.id == customizationId }
    }

    /**
     * Finds an Option in the children of the Offer by its Id.
     *
     * @param id The Id of the Option to be found.
     * @return The Option with the specified Id, or null if not found.
     */
    fun findOptionInChildrenById(id: Id): Option? {
        return optionsById[id]
    }

    /**
     * Finds a customization in the Offer or in the children of the Offer by its Id.
     *
     * @param id The Id of the Customization to be found.
     * @return The Customization with the specified Id, or null if not found.
     */
    fun findCustomizationInChildrenById(id: Id): Customization? {
        return customizationsById[id]
    }

    /**
     * Calculates the minimal price of the Offer.
     *
     * @return The minimal price.
     */
    fun minimalPrice(): Price {
        return Price(price.normalizedValue() + customizations.sumOf { it.minimalPrice().normalizedValue() })
    }

    fun getAllProducts(): List<Product> {
        return optionsById.values.map { option -> option.product } + product
    }
}
