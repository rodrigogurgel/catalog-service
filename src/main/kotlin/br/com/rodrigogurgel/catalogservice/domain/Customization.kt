package br.com.rodrigogurgel.catalogservice.domain

import java.util.UUID

data class Customization(
    val customizationId: UUID?,
    val storeId: UUID?,
    val name: String?,
    val description: String?,
    val minPermitted: Int?,
    val maxPermitted: Int?,
    val status: Status?,
    val index: Int?,
    val optionId: UUID? = null,
    val reference: String? = null
)
