package br.com.rodrigogurgel.catalogservice.application.port.out.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface ProductDatastoreOutputPort {
    fun create(storeId: Id, product: Product)
    fun findById(storeId: Id, productId: Id): Product?
    fun exists(storeId: Id, productId: Id): Boolean
    fun getIfNotExists(storeId: Id, productIds: List<Id>): List<Id>
    fun update(storeId: Id, product: Product)
    fun delete(storeId: Id, productId: Id)
}
