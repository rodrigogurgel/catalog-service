package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Product
import com.github.michaelbull.result.Result
import java.util.UUID

interface ProductDatastoreOutputPort {
    suspend fun create(product: Product): Result<Unit, Throwable>
    suspend fun update(product: Product): Result<Unit, Throwable>
    suspend fun delete(storeId: UUID, productId: UUID): Result<Unit, Throwable>
    suspend fun patch(product: Product): Result<Unit, Throwable>
    suspend fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable>
}
