package br.com.rodrigogurgel.catalog.domain

import java.util.UUID

data class Product(
    val productId: UUID?,
    val storeId: UUID?,
    val name: String?,
    val description: String?,
    val image: String?
)