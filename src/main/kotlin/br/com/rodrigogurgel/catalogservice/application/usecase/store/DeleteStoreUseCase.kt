package br.com.rodrigogurgel.catalogservice.application.usecase.store

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

interface DeleteStoreUseCase {
    fun execute(storeId: Id)
}
