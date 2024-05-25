package br.com.rodrigogurgel.catalogservice.application.port.out.datastore

import br.com.rodrigogurgel.catalogservice.domain.Product
import java.util.UUID

interface ProductDatastoreOutputPort {
    suspend fun create(product: Product)
    suspend fun update(product: Product)
    suspend fun delete(storeId: UUID, productId: UUID)
    suspend fun patch(product: Product)
    suspend fun find(storeId: UUID,productIds: Set<UUID>): List<Product>
}