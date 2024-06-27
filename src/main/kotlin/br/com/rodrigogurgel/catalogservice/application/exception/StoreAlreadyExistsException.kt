package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class StoreAlreadyExistsException(storeId: Id) :
    IllegalArgumentException("Store $storeId already exists")
