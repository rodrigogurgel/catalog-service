package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import org.springframework.stereotype.Component

@Component
class StoreDatastoreOutputPortMock : StoreDatastoreOutputPort {
    override fun exists(id: Id): Boolean = true
}
