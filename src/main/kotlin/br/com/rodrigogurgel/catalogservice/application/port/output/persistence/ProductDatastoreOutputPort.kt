package br.com.rodrigogurgel.catalogservice.application.port.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface ProductDatastoreOutputPort {
    fun create(storeId: Id, product: Product)
    fun findById(storeId: Id, productId: Id): Product?
    fun exists(productId: Id): Boolean
    fun exists(storeId: Id, productId: Id): Boolean
    fun getIfNotExists(storeId: Id, productIds: List<Id>): List<Id>
    fun update(storeId: Id, product: Product)
    fun delete(storeId: Id, productId: Id)
    fun getProducts(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Product>
    fun countProducts(storeId: Id, beginsWith: String?): Int
}
