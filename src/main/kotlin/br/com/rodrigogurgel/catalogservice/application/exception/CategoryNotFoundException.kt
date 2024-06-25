package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

data class CategoryNotFoundException(private val categoryId: Id) :
    IllegalArgumentException("Category with id $categoryId not found")
