package br.com.rodrigogurgel.catalogservice.application.port.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.entity.Store
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface StoreDatastoreOutputPort {
    fun exists(id: Id): Boolean
    fun create(store: Store)
}
