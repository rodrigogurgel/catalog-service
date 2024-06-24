package br.com.rodrigogurgel.catalogservice.application.port.out.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface StoreDatastoreOutputPort {
    fun exists(id: Id): Boolean
    fun createProduct(storeId: Id, product: Product)
}
