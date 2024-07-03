package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data

import java.util.UUID

data class ProductData(
    val productId: UUID,
    val storeId: UUID,
    val name: String,
    val description: String?,
    val imagePath: String?
)
