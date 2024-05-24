package br.com.rodrigogurgel.catalog.application.port.`in`

import br.com.rodrigogurgel.catalog.domain.Item
import com.github.michaelbull.result.Result
import java.util.UUID

interface ItemInputPort {
    fun create(item: Item): Result<Unit, Throwable>
    fun update(item: Item): Result<Unit, Throwable>
    fun delete(storeId: UUID, itemId: UUID): Result<Unit, Throwable>
    fun patch(item: Item): Result<Unit, Throwable>
    fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable>
    fun findByReference(reference: String): Result<Item, Throwable>
}