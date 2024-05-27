package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ItemDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Item
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ItemService(
    private val itemDatastoreOutputPort: ItemDatastoreOutputPort,
//    private val idempotencyOutputPort: IdempotencyOutputPort,
) : ItemInputPort {

    override suspend fun create(item: Item): Result<Unit, Throwable> = itemDatastoreOutputPort.create(item)

    override suspend fun update(item: Item): Result<Unit, Throwable> = itemDatastoreOutputPort.update(item)

    override suspend fun delete(storeId: UUID, itemId: UUID): Result<Unit, Throwable> =
        itemDatastoreOutputPort.delete(storeId, itemId)

    override suspend fun patch(item: Item): Result<Unit, Throwable> = itemDatastoreOutputPort.patch(item)

    override suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable> =
        itemDatastoreOutputPort.find(storeId, itemId)
}
