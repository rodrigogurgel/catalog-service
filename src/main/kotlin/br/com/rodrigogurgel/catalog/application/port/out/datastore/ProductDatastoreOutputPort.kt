package br.com.rodrigogurgel.catalog.application.port.out.datastore

import br.com.rodrigogurgel.catalog.domain.Product
import java.util.UUID

interface ProductDatastoreOutputPort {
    fun create(product: Product)
    fun update(product: Product)
    fun delete(storeId: UUID, productId: UUID)
    fun patch(product: Product)
    fun find(storeId: UUID,productIds: Set<UUID>): List<Product>
}