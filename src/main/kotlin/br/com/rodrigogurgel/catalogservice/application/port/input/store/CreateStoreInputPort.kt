package br.com.rodrigogurgel.catalogservice.application.port.input.store

import br.com.rodrigogurgel.catalogservice.application.exception.StoreAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.store.CreateStoreUseCase
import br.com.rodrigogurgel.catalogservice.domain.entity.Store

class CreateStoreInputPort(
    private val storeDatastoreOutputPort: StoreDatastoreOutputPort,
) : CreateStoreUseCase {
    override fun execute(store: Store) {
        if (storeDatastoreOutputPort.exists(store.id)) throw StoreAlreadyExistsException(store.id)
        storeDatastoreOutputPort.create(store)
    }
}
