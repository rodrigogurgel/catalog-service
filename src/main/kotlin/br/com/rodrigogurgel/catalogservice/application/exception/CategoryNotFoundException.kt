package br.com.rodrigogurgel.catalogservice.application.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CategoryNotFoundException private constructor(override val message: String?) : IllegalStateException(message) {
    constructor(
        storeId: Id,
        categoryId: Id,
    ) : this("Category with the id $categoryId and Store with the id $storeId not found")
}
