package br.com.rodrigogurgel.catalogservice.application

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.rest.StoreRestOutputPort
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.mockk.mockk

class CucumberContext {
    var result: Result<Any> = Result.failure(IllegalStateException())
    lateinit var storeId: Id
    val storeRestOutputPort: StoreRestOutputPort = mockk()
    val productDatastoreOutputPort: ProductDatastoreOutputPort = mockk()
    val categoryDatastoreOutputPort: CategoryDatastoreOutputPort = mockk()
    val offerDatastoreOutputPort: OfferDatastoreOutputPort = mockk()
}
