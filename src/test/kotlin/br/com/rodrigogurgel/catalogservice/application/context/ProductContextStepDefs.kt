package br.com.rodrigogurgel.catalogservice.application.context

import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import io.mockk.mockk

class ProductContextStepDefs {
    val productDatastoreOutputPort: ProductDatastoreOutputPort = mockk<ProductDatastoreOutputPort>()
    lateinit var product: Product
}
