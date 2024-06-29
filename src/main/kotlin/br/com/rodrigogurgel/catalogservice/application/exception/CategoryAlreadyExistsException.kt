package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CategoryAlreadyExistsException(categoryId: Id) :
    IllegalArgumentException("Category with the id $categoryId already exists")
