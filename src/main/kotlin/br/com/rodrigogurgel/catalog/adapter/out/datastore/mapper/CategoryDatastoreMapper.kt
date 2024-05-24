package br.com.rodrigogurgel.catalog.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalog.adapter.out.datastore.dto.CategoryDataStoreDTO
import br.com.rodrigogurgel.catalog.domain.Category

fun Category.toDatastoreDTO(): CategoryDataStoreDTO {
    return CategoryDataStoreDTO(
        categoryId = categoryId ?: throw RuntimeException(),
        storeId = storeId ?: throw RuntimeException(),
        name = name,
        status = status?.toDatastoreDTO(),
        index = index
    )
}

