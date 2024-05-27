package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.product

import java.util.UUID

data class ProductAlreadyExistsDatastoreException(val productId: UUID) :
    RuntimeException("Product $productId already exists")
