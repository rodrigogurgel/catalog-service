package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.math.BigDecimal
import java.util.UUID

data class OfferData(
    val offerId: UUID,
    val productId: UUID,
    val categoryId: UUID,
    val storeId: UUID,
    val name: String,
    val price: BigDecimal,
    val status: Status,
    val customizations: List<CustomizationData>,
) {
    fun getAllCustomizationsInChildren(): List<CustomizationData> {
        return customizations + customizations
            .flatMap { customizationData -> customizationData.getAllCustomizationsInChildren() }
    }

    fun getAllOptionsInChildren(): List<OptionData> {
        return customizations.flatMap { customizationData -> customizationData.getAllOptionsInChildren() }
    }
}
