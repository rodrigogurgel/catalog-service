package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategory
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategoryWith
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.config.DatabaseTestConfig
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [DatabaseTestConfig::class])
class CategoryDatastoreOutputPortAdapterTest {
    @Autowired
    private lateinit var categoryDatastoreOutputPortAdapter: CategoryDatastoreOutputPortAdapter

    @Test
    fun `Should create a Category successfully`() {
        val storeId = Id()
        val category = mockCategory()
        categoryDatastoreOutputPortAdapter.create(storeId, category)

        val result = categoryDatastoreOutputPortAdapter.findById(storeId, category.id)

        result.shouldNotBeNull()
        result shouldBe category
    }

    @Test
    fun `Should update a Category successfully`() {
        val storeId = Id()
        val category = mockCategory()
        categoryDatastoreOutputPortAdapter.create(storeId, category)

        category.status = Status.UNAVAILABLE
        category.name = Name(randomString(30))
        category.description = null

        categoryDatastoreOutputPortAdapter.update(storeId, category)

        val result = categoryDatastoreOutputPortAdapter.findById(storeId, category.id)

        result.shouldNotBeNull()
        result shouldBe category
    }

    @Test
    fun `Should return true when verify if the Category exists by Category Id`() {
        val storeId = Id()
        val category = mockCategory()
        categoryDatastoreOutputPortAdapter.create(storeId, category)

        val result = categoryDatastoreOutputPortAdapter.exists(category.id)

        result shouldBe true
    }

    @Test
    fun `Should return true when verify if the Category exists by Category Id and Store Id`() {
        val storeId = Id()
        val category = mockCategory()
        categoryDatastoreOutputPortAdapter.create(storeId, category)

        val result = categoryDatastoreOutputPortAdapter.exists(storeId, category.id)

        result shouldBe true
    }

    @Test
    fun `Should delete a Category successfully`() {
        val storeId = Id()
        val category = mockCategory()
        categoryDatastoreOutputPortAdapter.create(storeId, category)

        categoryDatastoreOutputPortAdapter.delete(storeId, category.id)

        val result = categoryDatastoreOutputPortAdapter.findById(storeId, category.id)

        result.shouldBeNull()
    }

    @Test
    fun `Should return false when verify if the Category doesn't exist by Category Id`() {
        val result = categoryDatastoreOutputPortAdapter.exists(Id())

        result shouldBe false
    }

    @Test
    fun `Should return false when verify if the Category doesn't exist by Category Id and Store Id`() {
        val result = categoryDatastoreOutputPortAdapter.exists(Id(), Id())

        result shouldBe false
    }

    @Test
    fun `Should count 10 categories successfully`() {
        val storeId = Id()
        val categories = List(10) {
            mockCategory()
        }

        categories.forEach { categoryDatastoreOutputPortAdapter.create(storeId, it) }

        val result = categoryDatastoreOutputPortAdapter.countCategories(storeId, null)

        result shouldBe 10
    }

    @Test
    fun `Should count 5 categories successfully using begins with`() {
        val storeId = Id()
        val categories = List(10) {
            mockCategory()
        } + List(5) {
            mockCategoryWith {
                name = Name("YES" + randomString(30))
            }
        }

        categories.forEach { categoryDatastoreOutputPortAdapter.create(storeId, it) }

        val result = categoryDatastoreOutputPortAdapter.countCategories(storeId, "YES")

        result shouldBe 5
    }

    @Test
    fun `Should get 10 categories successfully`() {
        val storeId = Id()
        val categories = List(10) {
            mockCategory()
        }

        categories.forEach { categoryDatastoreOutputPortAdapter.create(storeId, it) }

        val result = categoryDatastoreOutputPortAdapter.getCategories(storeId, 10, 0, null)

        result shouldContainAll categories
    }

    @Test
    fun `Should get 5 categories successfully using begins with`() {
        val storeId = Id()
        val categories = List(10) {
            mockCategory()
        } + List(5) {
            mockCategoryWith {
                name = Name("YES" + randomString(30))
            }
        }

        categories.forEach { categoryDatastoreOutputPortAdapter.create(storeId, it) }

        val result = categoryDatastoreOutputPortAdapter.getCategories(storeId, 20, 0, "YES")

        result shouldContainAll categories.filter { it.name.value.startsWith("YES") }
        result shouldHaveSize 5
    }
}
