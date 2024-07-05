package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CategoryNotFoundException(storeId: Id, categoryId: Id) :
    IllegalStateException("Category with the $categoryId and Store with the $storeId not found")
