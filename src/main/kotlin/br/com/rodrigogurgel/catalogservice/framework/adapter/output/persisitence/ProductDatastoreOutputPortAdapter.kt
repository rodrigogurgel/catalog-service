package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.toData
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.mapper.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence.repository.ProductRepository
import org.springframework.stereotype.Component

@Component
class ProductDatastoreOutputPortAdapter(
    private val productRepository: ProductRepository,
) : ProductDatastoreOutputPort {

    override fun create(storeId: Id, product: Product) {
        productRepository.create(product.toData(storeId.value))
    }

    override fun findById(storeId: Id, productId: Id): Product? {
        return productRepository.findById(storeId.value, productId.value)?.toEntity()
    }

    override fun exists(productId: Id): Boolean {
        return productRepository.exists(productId.value)
    }

    override fun exists(storeId: Id, productId: Id): Boolean {
        return productRepository.exists(storeId.value, productId.value)
    }

    override fun getIfNotExists(storeId: Id, productIds: List<Id>): List<Id> {
        return productRepository.getIfNotExists(storeId.value, productIds.map { id -> id.value })
            .map { value -> Id(value) }
    }

    override fun update(storeId: Id, product: Product) {
        productRepository.update(product.toData(storeId.value))
    }

    override fun delete(storeId: Id, productId: Id) {
        productRepository.delete(storeId.value, productId.value)
    }

    override fun getProducts(storeId: Id, limit: Int, offset: Int, beginsWith: String?): List<Product> {
        return productRepository.getProducts(storeId.value, limit, offset, beginsWith)
            .map { productData -> productData.toEntity() }
    }

    override fun countProducts(storeId: Id, beginsWith: String?): Int {
        return productRepository.countProducts(storeId.value, beginsWith)
    }

    override fun productIsInUse(productId: Id): Boolean {
        return productRepository.productIsInUse(productId.value)
    }
}
