package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.item

import java.util.UUID

data class ItemAlreadyExistsDatastoreException(val itemId: UUID) : RuntimeException("Item $itemId already exists")
