package br.com.rodrigogurgel.catalogservice.application.port.`in`

import br.com.rodrigogurgel.catalogservice.domain.Product
import com.github.michaelbull.result.Result
import java.util.UUID

interface ProductInputPort {
    suspend fun create(product: Product): Result<Unit, Throwable>
    suspend fun update(product: Product): Result<Unit, Throwable>
    suspend fun delete(storeId: UUID, productId: UUID): Result<Unit, Throwable>
    suspend fun patch(product: Product): Result<Unit, Throwable>
    suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable>
}
