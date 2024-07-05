package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status

data class Category(
    val id: Id,
    var name: Name,
    var description: Description?,
    var status: Status,
)
