package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.exception.OfferPriceZeroException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Status

class Offer private constructor(
    val id: Id,
    var product: Product,
    var status: Status,
) {
    private val rootCustomizationsById: MutableMap<Id, Customization> = mutableMapOf()

    private val customizationsById
        get() = customizations
            .flatMap { customization -> customization.getCustomizationsInChildren() }
            .associateBy { customization -> customization.id }

    private val optionsById
        get() = customizations
            .flatMap { customization -> customization.getOptionsInChildren() }
            .associateBy { option -> option.id }

    val customizations
        get() = rootCustomizationsById.values.toList()
    var price: Price = Price.ZERO
        set(value) {
            field = value
            validatePrice()
        }

    constructor(
        id: Id,
        product: Product,
        price: Price,
        status: Status,
        customizations: List<Customization>,
    ) : this(id, product, status) {

        this.product = product
        this.price = price
        this.status = status

        validatePrice()

        this.rootCustomizationsById.putAll(customizations.associateBy { customization -> customization.id })
    }

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
        if (rootCustomizationsById[customization.id] != null) {
            throw CustomizationAlreadyExistsException(customization.id)
        }

        rootCustomizationsById[customization.id] = customization
    }

    /**
     * Updates a customization in the Offer.
     *
     * @param customization the customization to be updated
     * @throws CustomizationNotFoundException if the customization with the specified id is not found in the Offer
     */
    fun updateCustomization(customization: Customization) {
        rootCustomizationsById[customization.id] ?: throw CustomizationNotFoundException(customization.id)
        rootCustomizationsById[customization.id] = customization
    }

    /**
     * Removes a customization from the Offer.
     *
     * @param customizationId the ID of the customization to be removed
     */
    fun removeCustomization(customizationId: Id) {
        rootCustomizationsById.remove(customizationId)
    }

    /**
     * Find an Option in the children of the Offer by its Id.
     *
     * @param id The Id of the Option to be found.
     * @return The Option with the specified Id, or null if not found.
     */
    fun findOptionInChildrenById(id: Id): Option? {
        return optionsById[id]
    }

    /**
     * Finds a customization in the Offer or in the childrens of the Offer by its Id.
     *
     * @param id The Id of the Customization to be found.
     * @return The Customization with the specified Id, or null if not found.
     */
    fun findCustomizationInChildrenById(id: Id): Customization? {
        return customizationsById[id]
    }

    fun minimalPrice(): Price {
        return Price(price.normalizedValue() + customizations.sumOf { it.minimalPrice().normalizedValue() })
    }
}