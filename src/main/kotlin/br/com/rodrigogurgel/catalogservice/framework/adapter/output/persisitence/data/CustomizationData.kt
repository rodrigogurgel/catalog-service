package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data

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
    val options: List<OptionData>,
)
