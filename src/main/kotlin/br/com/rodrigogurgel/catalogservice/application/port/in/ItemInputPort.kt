package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Item
import com.github.michaelbull.result.Result
import java.util.UUID

interface ItemInputPort {
    suspend fun create(item: Item): Result<Unit, Throwable>
    suspend fun update(item: Item): Result<Unit, Throwable>
    suspend fun delete(storeId: UUID, itemId: UUID): Result<Unit, Throwable>
    suspend fun patch(item: Item): Result<Unit, Throwable>
    suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable>
}