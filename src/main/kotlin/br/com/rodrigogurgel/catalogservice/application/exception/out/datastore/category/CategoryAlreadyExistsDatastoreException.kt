package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.category

import java.util.UUID

data class CategoryAlreadyExistsDatastoreException(val categoryId: UUID) :
    RuntimeException("Category $categoryId already exists")
