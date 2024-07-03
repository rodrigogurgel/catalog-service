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
    val customizations: List<CustomizationData>,
)