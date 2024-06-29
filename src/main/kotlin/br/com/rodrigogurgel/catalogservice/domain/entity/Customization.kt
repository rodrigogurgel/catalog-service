package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationMinPermittedException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationOptionsIsEmptyException
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status

class Customization private constructor(
    val id: Id,
    var name: Name,
    var description: Description?,
    var status: Status,
) {

    val options
        get() = optionById.values.toList()

    lateinit var quantity: Quantity
        private set

    private val optionById: MutableMap<Id, Option> = mutableMapOf()
    private val availableOptions
        get() = options.filter { option -> option.status == Status.AVAILABLE }

    constructor(
        id: Id,
        name: Name,
        description: Description?,
        quantity: Quantity,
        status: Status,
        options: List<Option>,
    ) : this(id, name, description, status) {

        this.quantity = quantity

        optionById.putAll(options.associateBy { option -> option.id })

        validateOptions()
        validateQuantity()
    }

    private fun validateOptions() {
        if (options.isEmpty()) throw CustomizationOptionsIsEmptyException(id)
    }

    private fun validateQuantity() {
        if (quantity.minPermitted > availableOptions.size) throw CustomizationMinPermittedException(id)
    }

    /**
     * Sets the quantity for the customization.
     *
     * @param quantity The quantity to be set.
     * @throws CustomizationOptionsIsEmptyException if the options list is empty.
     * @throws CustomizationMinPermittedException if the maxPermitted value of the quantity is greater than the size of the available options list.
     */
    fun setQuantity(quantity: Quantity) {
        this.quantity = quantity
        validateQuantity()
    }

    /**
     * Adds an option to the customization.
     *
     * @param option The option to be added.
     * @throws OptionAlreadyExistsException if an option with the same ID already exists in the customization.
     */
    fun addOption(option: Option) {
        if (optionById[option.id] != null) throw OptionAlreadyExistsException(option.id)
        optionById[option.id] = option
    }

    /**
     * Update an option in the customization.
     *
     * @param option The option to be updated.
     * @throws OptionNotFoundException if the option with the given id is not found.
     */
    fun updateOption(option: Option) {
        optionById[option.id] ?: throw OptionNotFoundException(option.id)

        validateOptions()
        validateQuantity()

        optionById[option.id] = option
    }

    /**
     * Removes the specified option from the customization.
     *
     * @param optionId The ID of the option to be removed.
     * @throws OptionNotFoundException if the option with the specified ID is not found.
     */
    fun removeOption(optionId: Id) {
        optionById[optionId] ?: throw OptionNotFoundException(optionId)

        validateOptions()
        validateQuantity()

        optionById.remove(optionId)
    }

    /**
     * Retrieves all the customizations that are present in the children of the current customization.
     *
     * @return A list of Customization objects that are found in the children of the current customization.
     */
    fun getCustomizationsInChildren(): List<Customization> {
        return options.flatMap { option -> option.getCustomizationsInChildren() } + this
    }

    /**
     * Returns the list of options found in the children of this option.
     *
     * @return The list of options found in the children of this option.
     */
    fun getOptionsInChildren(): List<Option> {
        return options.flatMap { option -> option.getOptionsInChildren() }
    }

    fun minimalPrice(): Price {
        return options.map { it.minimalPrice() }
            .sortedBy { it.normalizedValue() }
            .take(quantity.minPermitted)
            .sumOf { it.normalizedValue() }
            .let { Price(it) }
    }
}
