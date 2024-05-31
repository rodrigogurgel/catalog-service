package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Item
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import com.github.michaelbull.result.Result
import java.util.UUID

interface ItemInputPort {
    suspend fun create(transaction: Transaction<Item>): Result<Unit, Throwable>
    suspend fun update(transaction: Transaction<Item>): Result<Unit, Throwable>
    suspend fun delete(transaction: Transaction<Item>): Result<Unit, Throwable>

    suspend fun find(storeId: UUID, itemId: UUID): Result<Item, Throwable>
    suspend fun searchByProductId(storeId: UUID, productId: UUID): Result<List<Item>, Throwable>
    suspend fun searchByCategoryId(storeId: UUID, categoryId: UUID): Result<List<Item>, Throwable>
}
