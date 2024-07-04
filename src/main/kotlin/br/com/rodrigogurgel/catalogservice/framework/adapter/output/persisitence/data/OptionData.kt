package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.math.BigDecimal
import java.util.UUID

data class OptionData(
    val optionId: UUID,
    val offerId: UUID,
    val storeId: UUID,
    val productId: UUID,
    val customizationId: UUID,
    val minPermitted: Int,
    val maxPermitted: Int,
    val price: BigDecimal,
    val status: Status,
    var customizations: List<CustomizationData> = emptyList(),
) {
    fun getAllCustomizationsInChildren(): List<CustomizationData> {
        return customizations
            .flatMap { customizationData -> customizationData.getAllCustomizationsInChildren() + customizationData }
    }

    fun getAllOptionsInChildren(): List<OptionData> {
        return customizations.flatMap { customizationData -> customizationData.getAllOptionsInChildren() }
    }
}
