package br.com.rodrigogurgel.catalogservice.application.usecase.store

import br.com.rodrigogurgel.catalogservice.domain.entity.Store

interface UpdateStoreUseCase {
    fun execute(store: Store)
}
