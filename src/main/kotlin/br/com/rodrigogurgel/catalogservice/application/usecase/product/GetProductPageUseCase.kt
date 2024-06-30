package br.com.rodrigogurgel.catalogservice.application.usecase.product

import br.com.rodrigogurgel.catalogservice.domain.entity.Page
import br.com.rodrigogurgel.catalogservice.domain.entity.Product

interface GetProductPageUseCase {
    fun execute(limit: Int, offset: Int): Page<Product>
}
