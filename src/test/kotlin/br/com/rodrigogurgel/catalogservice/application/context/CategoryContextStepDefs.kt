package br.com.rodrigogurgel.catalogservice.application.context

import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import io.mockk.mockk

class CategoryContextStepDefs {
    val categoryDatastoreOutputPort: CategoryDatastoreOutputPort = mockk<CategoryDatastoreOutputPort>()
    lateinit var category: Category
}
