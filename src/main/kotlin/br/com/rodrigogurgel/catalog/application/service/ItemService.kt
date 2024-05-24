package br.com.rodrigogurgel.catalog.application.service

import br.com.rodrigogurgel.catalog.domain.Item
import br.com.rodrigogurgel.catalog.application.port.`in`.ItemInputPort
import br.com.rodrigogurgel.catalog.application.port.out.datastore.ItemDatastoreOutputPort
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class ItemService(
    private val itemDatastoreOutputPort: ItemDatastoreOutputPort,
) : ItemInputPort {

    override fun create(item: Item): Result<Unit, Throwable> = runCatching {
        itemDatastoreOutputPort.create(item)
    }.onFailure {
        throw it
    }

    override fun update(item: Item): Result<Unit, Throwable> = runCatching {
        itemDatastoreOutputPort.update(item)
    }

    override fun delete(storeId: UUID, itemId: UUID): Result<Unit, Throwable> = runCatching {
        itemDatastoreOutputPort.delete(storeId, itemId)
    }

    override fun patch(item: Item): Result<Unit, Throwable> = runCatching {
        itemDatastoreOutputPort.patch(item)
    }

    override fun findByReference(reference: String): Result<Item, Throwable> = runCatching {
        itemDatastoreOutputPort.findByReference(reference)
    }

    override fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable> = runCatching {
        itemDatastoreOutputPort.find(storeId, itemId)
    }

}