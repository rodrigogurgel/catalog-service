package br.com.rodrigogurgel.catalog.application.port.`in`

import br.com.rodrigogurgel.catalog.domain.Product
import com.github.michaelbull.result.Result
import java.util.UUID

interface ProductInputPort {
    fun create(product: Product): Result<Unit, Throwable>
    fun update(product: Product): Result<Unit, Throwable>
    fun delete(storeId: UUID, productId: UUID): Result<Unit, Throwable>
    fun patch(product: Product): Result<Unit, Throwable>
    fun find(storeId: UUID, productIds: Set<UUID>): Result<List<Product>, Throwable>
}