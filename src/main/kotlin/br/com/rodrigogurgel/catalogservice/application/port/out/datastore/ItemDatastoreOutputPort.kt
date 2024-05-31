package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Item
import com.github.michaelbull.result.Result
import java.util.UUID

interface ItemDatastoreOutputPort {
    suspend fun create(item: Item): Result<Unit, Throwable>
    suspend fun update(item: Item): Result<Unit, Throwable>
    suspend fun delete(storeId: UUID, itemId: UUID): Result<Unit, Throwable>
    suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable>
    suspend fun searchByProductId(storeId: UUID, productId: UUID): Result<List<Item>, Throwable>
    suspend fun searchByCategoryId(storeId: UUID, categoryId: UUID): Result<List<Item>, Throwable>
}
