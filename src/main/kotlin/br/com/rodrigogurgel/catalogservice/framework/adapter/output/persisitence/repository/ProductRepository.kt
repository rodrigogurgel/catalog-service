package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository

import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.data.ProductData
import java.util.UUID

interface ProductRepository {
    fun create(productData: ProductData)
    fun findById(storeId: UUID, productId: UUID): ProductData?
    fun exists(productId: UUID): Boolean
    fun exists(storeId: UUID, productId: UUID): Boolean
    fun getIfNotExists(storeId: UUID, productIds: List<UUID>): List<UUID>
    fun update(productData: ProductData)
    fun delete(storeId: UUID, productId: UUID)
    fun getProducts(storeId: UUID, limit: Int, offset: Int, beginsWith: String?): List<ProductData>
    fun countProducts(storeId: UUID, beginsWith: String?): Int
    fun productIsInUse(productId: UUID): Boolean
    fun getAllProductByOfferId(offerId: UUID): List<ProductData>
}
