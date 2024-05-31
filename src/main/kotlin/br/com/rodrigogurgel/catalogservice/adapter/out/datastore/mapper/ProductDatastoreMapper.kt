package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.ProductDatastoreDTO
import br.com.rodrigogurgel.catalogservice.domain.Product

fun Product.toDatastoreDTO(): ProductDatastoreDTO {
    return ProductDatastoreDTO(
        productId = productId,
        name = name,
        storeId = storeId,
        image = image,
        description = description
    )
}

fun ProductDatastoreDTO.toDomain(): Product {
    return Product(
        productId = productId,
        name = name,
        storeId = storeId,
        image = image,
        description = description,
    )
}
