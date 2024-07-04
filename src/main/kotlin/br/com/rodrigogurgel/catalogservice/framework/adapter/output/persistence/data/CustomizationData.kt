package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.util.UUID

data class CustomizationData(
    val customizationId: UUID,
    val offerId: UUID,
    val storeId: UUID,
    val optionId: UUID?,
    val name: String,
    val description: String?,
    val minPermitted: Int,
    val maxPermitted: Int,
    val status: Status,
    var options: List<OptionData> = emptyList(),
) {
    fun getAllCustomizationsInChildren(): List<CustomizationData> {
        return options.flatMap { optionData -> optionData.getAllCustomizationsInChildren() }
    }

    fun getAllOptionsInChildren(): List<OptionData> {
        return options + options.flatMap { optionData -> optionData.getAllOptionsInChildren() }
    }
}
