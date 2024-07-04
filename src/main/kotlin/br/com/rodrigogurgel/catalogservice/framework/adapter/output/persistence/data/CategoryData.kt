package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence.data

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import java.util.UUID

data class CategoryData(
    val categoryId: UUID,
    val storeId: UUID,
    val name: String,
    val description: String?,
    val status: Status
)
