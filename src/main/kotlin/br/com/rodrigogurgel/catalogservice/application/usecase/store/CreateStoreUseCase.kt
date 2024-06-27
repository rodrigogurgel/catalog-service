package br.com.rodrigogurgel.catalogservice.application.usecase.store

import br.com.rodrigogurgel.catalogservice.domain.entity.Store

interface CreateStoreUseCase {
    fun execute(store: Store)
}
