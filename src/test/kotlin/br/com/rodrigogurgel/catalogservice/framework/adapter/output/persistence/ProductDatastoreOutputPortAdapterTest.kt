package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategory
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.config.DatabaseTestConfig
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
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
class ProductDatastoreOutputPortAdapterTest {

    @Autowired
    private lateinit var productDatastoreOutputPortAdapter: ProductDatastoreOutputPortAdapter

    @Autowired
    private lateinit var offerDatastoreOutputPortAdapter: OfferDatastoreOutputPortAdapter

    @Autowired
    private lateinit var categoryDatastoreOutputPortAdapter: CategoryDatastoreOutputPortAdapter

    @Test
    fun `Should create a Product Successfully`() {
        val storeId = Id()
        val product = mockProduct()

        productDatastoreOutputPortAdapter.create(storeId, product)

        val result = productDatastoreOutputPortAdapter.findById(storeId, product.id)

        result.shouldNotBeNull()
        result shouldBe product
    }

    @Test
    fun `Should return null when Product does not exists`() {
        val result = productDatastoreOutputPortAdapter.findById(Id(), Id())

        result.shouldBeNull()
    }

    @Test
    fun `Should update a Product successfully`() {
        val storeId = Id()
        val product = mockProduct()

        productDatastoreOutputPortAdapter.create(storeId, product)

        product.name = Name(randomString(30))
        product.description = Description(randomString(1000))
        product.image = null

        productDatastoreOutputPortAdapter.update(storeId, product)

        val result = productDatastoreOutputPortAdapter.findById(storeId, product.id)

        result.shouldNotBeNull()
        result shouldBe product
    }

    @Test
    fun `Should delete a Product successfully`() {
        val storeId = Id()
        val product = mockProduct()

        productDatastoreOutputPortAdapter.create(storeId, product)

        val deletedProduct = productDatastoreOutputPortAdapter.findById(storeId, product.id)

        deletedProduct.shouldNotBeNull()

        productDatastoreOutputPortAdapter.delete(storeId, product.id)

        val deletedProductAfterDeletion = productDatastoreOutputPortAdapter.findById(storeId, product.id)

        deletedProductAfterDeletion shouldBe null
    }

    @Test
    fun `Should return Product Id if not exists`() {
        val storeId = Id()
        val productNotExists = Id()
        val product = mockProduct()

        productDatastoreOutputPortAdapter.create(storeId, product)

        val result = productDatastoreOutputPortAdapter.getIfNotExists(listOf(product.id, productNotExists))

        result shouldContain productNotExists
        result shouldNotContain product.id
    }

    @Test
    fun `Should return true when the Product exists`() {
        val storeId = Id()
        val product = mockProduct()

        productDatastoreOutputPortAdapter.create(storeId, product)

        val result = productDatastoreOutputPortAdapter.exists(product.id)

        result shouldBe true
    }

    @Test
    fun `Should return false when the Product does not exists`() {
        val result = productDatastoreOutputPortAdapter.exists(Id())

        result shouldBe false
    }

    @Test
    fun `Should return true when the Product is in use`() {
        val storeId = Id()
        val category = mockCategory()
        val product = mockProduct()
        val offer = mockOfferWith {
            this.product = product
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)
        productDatastoreOutputPortAdapter.create(storeId, product)
        offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)

        val result = productDatastoreOutputPortAdapter.productIsInUse(product.id)

        result shouldBe true
    }

    @Test
    fun `Should return false when the Product is in use`() {
        val result = productDatastoreOutputPortAdapter.productIsInUse(Id())

        result shouldBe false
    }

    @Test
    fun `Should return true when the Product does not exists by Product Id and Store Id`() {
        val storeId = Id()
        val product = mockProduct()

        productDatastoreOutputPortAdapter.create(storeId, product)

        val result = productDatastoreOutputPortAdapter.exists(storeId, product.id)

        result shouldBe true
    }

    @Test
    fun `Should return false when the Product does not exists by Product Id and Store Id`() {
        val result = productDatastoreOutputPortAdapter.exists(Id(), Id())

        result shouldBe false
    }

    @Test
    fun `Should count 10 Products with begins with null`() {
        val storeId = Id()
        val products = List(10) { mockProduct() }

        products.forEach {
            productDatastoreOutputPortAdapter.create(storeId, it)
        }

        val result = productDatastoreOutputPortAdapter.countProducts(storeId, null)

        result shouldBe 10
    }

    @Test
    fun `Should count 5 Products with begins with is YES`() {
        val storeId = Id()
        val products = List(10) { mockProduct() } + List(5) {
            mockProductWith {
                name = Name("YES" + randomString(30))
            }
        }

        products.forEach {
            productDatastoreOutputPortAdapter.create(storeId, it)
        }

        val result = productDatastoreOutputPortAdapter.countProducts(storeId, "YES")

        result shouldBe 5
    }

    @Test
    fun `Should get 15 Products`() {
        val storeId = Id()
        val productsYES = List(5) {
            mockProductWith {
                name = Name("YES" + randomString(30))
                description = null
            }
        }
        val products = List(10) { mockProduct() } + productsYES

        products.forEach {
            productDatastoreOutputPortAdapter.create(storeId, it)
        }

        val result = productDatastoreOutputPortAdapter.getProducts(storeId, 20, 0, null)

        result shouldContainAll products
        result shouldHaveSize 15
    }

    @Test
    fun `Should get 5 Products with begins with is YES`() {
        val storeId = Id()
        val productsYES = List(5) {
            mockProductWith {
                name = Name("YES" + randomString(30))
            }
        }
        val products = List(10) { mockProduct() } + productsYES

        products.forEach {
            productDatastoreOutputPortAdapter.create(storeId, it)
        }

        val result = productDatastoreOutputPortAdapter.getProducts(storeId, 20, 0, "YES")

        result shouldContainAll productsYES
        result shouldHaveSize 5
    }
}
