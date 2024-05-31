package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.item

import java.util.UUID

data class ItemNotFoundDatastoreException(val storeId: UUID, val itemId: UUID) :
    RuntimeException("Item $itemId not found in store $storeId")
