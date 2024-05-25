package br.com.rodrigogurgel.catalogservice.domain

import java.math.BigDecimal
import java.util.UUID

data class Option(
    val optionId: UUID?,
    val storeId: UUID?,
    val customizationId: UUID?,
    val productId: UUID?,
    val price: BigDecimal?,
    val status: Status?,
    val minPermitted: Int?,
    val maxPermitted: Int?,
    val index: Int?,
    val reference: String? = null,
)