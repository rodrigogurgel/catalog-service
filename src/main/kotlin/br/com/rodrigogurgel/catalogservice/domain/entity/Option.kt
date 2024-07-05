package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

class Option(
    val id: Id,
    var product: Product,
    var price: Price,
    var quantity: Quantity,
    var status: Status,
    val customizations: MutableList<Customization>,
) {
    /**
     * Retrieves all the customizations that are present in the children of the current customization.
     *
     * @return A list of Customization objects that are found in the children of the current customization.
     */
    fun getAllCustomizationsInChildren(): List<Customization> {
        return customizations + customizations.flatMap { it.getAllCustomizationsInChildren() }
    }

    /**
     * Retrieves all the options in the current object and its child customizations.
     *
     * @return A list of options present in the current object and its child customizations.
     */
    fun getAllOptionsInChildren(): List<Option> {
        return customizations.flatMap { it.getAllOptionsInChildren() }
    }

    /**
     * Adds a customization to the Option.
     *
     * @param customization The customization to be added.
     * @throws CustomizationAlreadyExistsException if a customization with the same ID already exists in the Option.
     */
    fun addCustomization(customization: Customization) {
        if (customizations.any { it.id == customization.id }) {
            throw CustomizationAlreadyExistsException(customization.id)
        }

        customizations.add(customization)
    }

    /**
     * Updates a customization with the given data.
     *
     * @param customization The customization object containing the updated information.
     * @throws CustomizationNotFoundException if the customization with the specified ID is not found.
     */
    fun updateCustomization(customization: Customization) {
        val index = customizations.indexOfFirst { it.id == customization.id }
        if (index == -1) throw CustomizationNotFoundException(customization.id)
        customizations[index] = customization
    }

    /**
     * Remove a customization with the given customization ID.
     *
     * @param customizationId The ID of the customization to be removed.
     */
    fun removeCustomization(customizationId: Id) {
        customizations.removeIf { it.id == customizationId }
    }

    /**
     * Calculates the minimal price for the Option.
     *
     * @return The minimal price.
     */
    fun minimalPrice(): Price {
        val minPermittedOrOne = if (quantity.minPermitted == 0) 1 else quantity.minPermitted

        return Price(
            (price.normalizedValue() * minPermittedOrOne.toBigDecimal()) + customizations.sumOf {
                it.minimalPrice().normalizedValue()
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Option) return false

        return EqualsBuilder()
            .append(id, other.id)
            .append(product, other.product)
            .append(price.normalizedValue(), other.price.normalizedValue())
            .append(quantity, other.quantity)
            .append(status, other.status)
            .append(customizations, other.customizations)
            .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder()
            .append(id)
            .append(product)
            .append(price.normalizedValue())
            .append(quantity)
            .append(status)
            .append(customizations)
            .toHashCode()
    }
}
