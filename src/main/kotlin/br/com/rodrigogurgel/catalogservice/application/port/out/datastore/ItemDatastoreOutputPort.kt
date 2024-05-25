package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Item
import java.util.UUID

interface ItemDatastoreOutputPort {
    suspend fun create(item: Item)
    suspend fun update(item: Item)
    suspend fun delete(storeId: UUID, itemId: UUID)
    suspend fun patch(item: Item)
    suspend fun find(storeId: UUID, itemId: UUID): Item
}