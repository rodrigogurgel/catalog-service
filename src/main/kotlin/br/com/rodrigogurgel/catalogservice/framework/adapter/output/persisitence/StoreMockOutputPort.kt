package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persisitence

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import org.springframework.stereotype.Repository

@Repository
class StoreMockOutputPort : StoreDatastoreOutputPort {
    override fun exists(id: Id): Boolean = true
}
