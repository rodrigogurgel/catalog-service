package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.product

import java.util.UUID

data class ProductNotFoundDatastoreException(val storeId: UUID, val productId: UUID) :
    RuntimeException("Product $productId not found in store $storeId")
