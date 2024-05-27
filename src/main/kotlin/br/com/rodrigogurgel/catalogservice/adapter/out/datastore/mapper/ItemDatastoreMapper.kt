package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ItemDatastoreDTO
import br.com.rodrigogurgel.catalogservice.domain.Item

fun Item.toDatastoreDTO(): ItemDatastoreDTO {
    return ItemDatastoreDTO(
        itemId = itemId,
        storeId = storeId,
        categoryId = categoryId,
        productId = productId,
        price = price,
        status = status?.toDatastoreDTO(),
        index = index,
        reference = reference
    )
}

fun ItemDatastoreDTO.toDomain(): Item {
    return Item(
        itemId = itemId,
        storeId = storeId,
        categoryId = categoryId,
        productId = productId,
        price = price,
        status = status!!.toDomain(),
        index = index,
        reference = reference
    )
}
