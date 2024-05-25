package br.com.rodrigogurgel.catalogservice.domain

import java.util.UUID

data class Category(
    val categoryId: UUID?,
    val storeId: UUID?,
    val name: String?,
    val status: Status?,
    val index: Int?
)
