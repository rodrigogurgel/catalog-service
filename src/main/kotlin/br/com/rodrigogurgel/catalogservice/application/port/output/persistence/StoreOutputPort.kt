package br.com.rodrigogurgel.catalogservice.application.port.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface StoreOutputPort {
    fun exists(id: Id): Boolean
}
