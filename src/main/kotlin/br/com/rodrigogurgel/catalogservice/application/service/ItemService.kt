package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.application.common.toUUID
import br.com.rodrigogurgel.catalogservice.application.port.`in`.IdempotencyInputPort
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ItemDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.Item
import com.github.michaelbull.result.Result
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ItemService(
    private val itemDatastoreOutputPort: ItemDatastoreOutputPort,
    private val idempotencyInputPort: IdempotencyInputPort,
) : ItemInputPort {

    override suspend fun create(idempotencyId: UUID, correlationId: UUID, item: Item): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            item.storeId!!
        ) { itemDatastoreOutputPort.create(item) }

    override suspend fun update(idempotencyId: UUID, correlationId: UUID, item: Item): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            item.storeId!!
        ) { itemDatastoreOutputPort.update(item) }

    override suspend fun delete(idempotencyId: UUID, correlationId: UUID, item: Item): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            item.storeId!!
        ) { itemDatastoreOutputPort.delete(item.storeId.toString().toUUID(), item.itemId.toString().toUUID()) }

    override suspend fun patch(idempotencyId: UUID, correlationId: UUID, item: Item): Result<Unit, Throwable> =
        idempotencyInputPort.runWithIdempotency(
            idempotencyId,
            correlationId,
            item.storeId!!
        ) { itemDatastoreOutputPort.patch(item) }

    override suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable> =
        itemDatastoreOutputPort.find(storeId, itemId)
}
