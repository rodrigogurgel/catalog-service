package br.com.rodrigogurgel.catalog.domain

import java.math.BigDecimal
import java.util.UUID

data class Item(
    val itemId: UUID?,
    val storeId: UUID?,
    val categoryId: UUID?,
    val productId: UUID?,
    val price: BigDecimal?,
    val status: Status?,
    val index: Int?,
    val reference: String? = null
)