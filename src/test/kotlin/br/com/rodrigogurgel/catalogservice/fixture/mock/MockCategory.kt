package br.com.rodrigogurgel.catalogservice.fixture.mock

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.randomString

data class MockCategory(
    var id: Id = Id(),
    var name: Name = Name(randomString(30)),
    var description: Description = Description(randomString(100)),
    var status: Status = Status.AVAILABLE,
)

fun mockCategory(): Category = MockCategory().run {
    Category(id, name, description, status)
}

fun mockCategoryWith(block: MockCategory.() -> Unit): Category = MockCategory()
    .apply(block)
    .run { Category(id, name, description, status) }
