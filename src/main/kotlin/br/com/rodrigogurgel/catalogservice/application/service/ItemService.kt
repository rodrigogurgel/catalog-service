package br.com.rodrigogurgel.catalogservice.application.service

import br.com.rodrigogurgel.catalogservice.domain.Item
import br.com.rodrigogurgel.catalogservice.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalogservice.application.port.out.datastore.ItemDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.coroutines.runSuspendCatching
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class ItemService(
    private val itemDatastoreOutputPort: ItemDatastoreOutputPort,
) : ItemInputPort {

    override suspend fun create(item: Item): Result<Unit, Throwable> = runSuspendCatching {
        itemDatastoreOutputPort.create(item)
    }.onFailure {
        throw it
    }

    override suspend fun update(item: Item): Result<Unit, Throwable> = runSuspendCatching {
        itemDatastoreOutputPort.update(item)
    }.onFailure {
        throw it
    }

    override suspend fun delete(storeId: UUID, itemId: UUID): Result<Unit, Throwable> = runSuspendCatching {
        itemDatastoreOutputPort.delete(storeId, itemId)
    }.onFailure {
        throw it
    }

    override suspend fun patch(item: Item): Result<Unit, Throwable> = runSuspendCatching {
        itemDatastoreOutputPort.patch(item)
    }.onFailure {
        throw it
    }

    override suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable> = runSuspendCatching {
        itemDatastoreOutputPort.find(storeId, itemId)
    }.onFailure {
        throw it
    }

}