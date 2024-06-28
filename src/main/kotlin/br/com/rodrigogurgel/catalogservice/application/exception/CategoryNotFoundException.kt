package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CategoryNotFoundException private constructor(override val message: String?) : IllegalStateException(message) {
    constructor(storeId: Id, categoryId: Id) : this("Category with id $categoryId and Store id $storeId not found")
    constructor(categoryId: Id) : this("Category with id $categoryId not found")
}
