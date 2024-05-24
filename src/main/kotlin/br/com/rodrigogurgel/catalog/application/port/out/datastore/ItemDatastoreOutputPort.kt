package br.com.rodrigogurgel.catalog.application.port.out.datastore

import br.com.rodrigogurgel.catalog.domain.Item
import java.util.UUID

interface ItemDatastoreOutputPort {
    fun create(item: Item)
    fun update(item: Item)
    fun delete(storeId: UUID, itemId: UUID)
    fun patch(item: Item)
    fun findByReference(reference: String): Item
    fun find(storeId: UUID, itemId: UUID): Item
}