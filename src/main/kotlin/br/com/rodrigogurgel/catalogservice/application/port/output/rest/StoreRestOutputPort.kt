package br.com.rodrigogurgel.catalogservice.application.port.output.rest

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface StoreRestOutputPort {
    fun exists(id: Id): Boolean
}
