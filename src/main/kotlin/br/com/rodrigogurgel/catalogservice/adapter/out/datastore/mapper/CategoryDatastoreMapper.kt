package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.CategoryDataStoreDTO
import br.com.rodrigogurgel.catalogservice.domain.Category

fun Category.toDatastoreDTO(): CategoryDataStoreDTO {
    return CategoryDataStoreDTO(
        categoryId = categoryId,
        storeId = storeId,
        name = name,
        status = status?.toDatastoreDTO(),
        index = index
    )
}
