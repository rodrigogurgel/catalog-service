package br.com.rodrigogurgel.catalogservice.application

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.mockk.mockk

class CucumberContext {
    var result: Result<Any> = Result.failure(IllegalStateException())
    lateinit var storeId: Id
    val storeOutputPort: StoreOutputPort = mockk()
    val productOutputPort: ProductOutputPort = mockk()
    val categoryOutputPort: CategoryOutputPort = mockk()
    val offerOutputPort: OfferOutputPort = mockk()
    val storeProducts: MutableMap<Id, Product> = mutableMapOf()
}
