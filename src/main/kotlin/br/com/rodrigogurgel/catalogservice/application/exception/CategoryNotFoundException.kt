package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

data class CategoryNotFoundException(private val categoryId: Id) :
    RuntimeException("Category with id $categoryId not found")
