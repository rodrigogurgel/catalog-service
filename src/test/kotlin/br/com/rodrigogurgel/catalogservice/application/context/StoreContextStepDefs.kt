package br.com.rodrigogurgel.catalogservice.application.context

import br.com.rodrigogurgel.catalogservice.application.port.out.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Store
import io.mockk.mockk

class StoreContextStepDefs {
    val storeDatastoreOutputPort: StoreDatastoreOutputPort = mockk<StoreDatastoreOutputPort>()
    lateinit var store: Store
}
