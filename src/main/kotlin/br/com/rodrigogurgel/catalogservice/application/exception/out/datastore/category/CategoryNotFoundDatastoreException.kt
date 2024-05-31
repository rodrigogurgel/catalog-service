package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.category

import java.util.UUID

data class CategoryNotFoundDatastoreException(val storeId: UUID, val categoryId: UUID) :
    RuntimeException("Category $categoryId not found in store $storeId")
