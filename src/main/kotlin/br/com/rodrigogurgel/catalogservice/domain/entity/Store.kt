package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name

data class Store(
    val id: Id,
    val name: Name,
    val description: Description?,
    val categories: MutableMap<Id, Category>,
    val products: MutableMap<Id, Product>,
)
