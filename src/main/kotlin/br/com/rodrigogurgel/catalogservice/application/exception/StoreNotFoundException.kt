package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class StoreNotFoundException(storeId: Id) :
    IllegalStateException("Store with the ${storeId.value} not found")
