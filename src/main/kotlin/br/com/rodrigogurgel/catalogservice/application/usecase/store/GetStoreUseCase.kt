package br.com.rodrigogurgel.catalogservice.application.usecase.store

import br.com.rodrigogurgel.catalogservice.domain.entity.Store
import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface GetStoreUseCase {
    fun execute(storeId: Id): Store
}
