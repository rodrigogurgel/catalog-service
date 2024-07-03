package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status

class Option private constructor(
    val id: Id,
    var product: Product,
    var quantity: Quantity,
    var price: Price,
    var status: Status,
) {
    private val customizationById: MutableMap<Id, Customization> = mutableMapOf()

    constructor(
        id: Id,
        product: Product,
        price: Price,
        quantity: Quantity,
        status: Status,
        customizations: List<Customization>,
    ) : this(id, product, quantity, price, status) {
        customizationById.putAll(customizations.associateBy { customization -> customization.id })
    }

    var customizations
        get() = customizationById.values.toList()
        set(value) {
            customizationById.clear()
            customizationById.putAll(value.associateBy { customization -> customization.id })
        }

    /**
     * Retrieves all the customizations that are present in the children of the current customization.
     *
     * @return A list of Customization objects that are found in the children of the current customization.
     */
    fun getCustomizationsInChildren(): List<Customization> {
        return customizations.flatMap { customization -> customization.getCustomizationsInChildren() }
    }

    /**
     * Retrieves all the options in the current object and its child customizations.
     *
     * @return A list of options present in the current object and its child customizations.
     */
    fun getOptionsInChildren(): List<Option> {
        return customizations.flatMap { customization -> customization.getOptionsInChildren() } + this
    }

    /**
     * Adds a customization to the Option.
     *
     * @param customization The customization to be added.
     * @throws CustomizationAlreadyExistsException if a customization with the same ID already exists in the Option.
     */
    fun addCustomization(customization: Customization) {
        if (customizationById[customization.id] != null) throw CustomizationAlreadyExistsException(customization.id)
        customizationById[customization.id] = customization
    }

    /**
     * Updates a customization with the given data.
     *
     * @param customization The customization object containing the updated information.
     * @throws CustomizationNotFoundException if the customization with the specified ID is not found.
     */
    fun updateCustomization(customization: Customization) {
        customizationById[customization.id] ?: throw CustomizationNotFoundException(customization.id)
        customizationById[customization.id] = customization
    }

    /**
     * Remove a customization with the given customization ID.
     *
     * @param customizationId The ID of the customization to be removed.
     */
    fun removeCustomization(customizationId: Id) {
        customizationById.remove(customizationId)
    }

    fun minimalPrice(): Price {
        val minPermittedOrOne = if (quantity.minPermitted == 0) 1 else quantity.minPermitted

        return Price(
            (price.normalizedValue() * minPermittedOrOne.toBigDecimal()) + customizations.sumOf {
                it.minimalPrice().normalizedValue()
            }
        )
    }
}
