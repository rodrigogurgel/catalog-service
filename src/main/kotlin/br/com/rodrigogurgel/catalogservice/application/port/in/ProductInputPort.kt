package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Product
import br.com.rodrigogurgel.catalogservice.domain.Transaction
import com.github.michaelbull.result.Result
import java.util.UUID

interface ProductInputPort {
    suspend fun create(transaction: Transaction<Product>): Result<Unit, Throwable>
    suspend fun update(transaction: Transaction<Product>): Result<Unit, Throwable>
    suspend fun delete(transaction: Transaction<Product>): Result<Unit, Throwable>

    suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable>
}
