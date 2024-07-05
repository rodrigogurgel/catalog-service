package br.com.rodrigogurgel.catalogservice.framework.adapter.output.persistence

import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategory
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomization
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOffer
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.config.DatabaseTestConfig
import io.kotest.matchers.equals.shouldBeEqual
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
class OfferDatastoreOutputPortAdapterTest {
    @Autowired
    private lateinit var categoryDatastoreOutputPortAdapter: CategoryDatastoreOutputPortAdapter

    @Autowired
    private lateinit var productDatastoreOutputPortAdapter: ProductDatastoreOutputPortAdapter

    @Autowired
    private lateinit var offerDatastoreOutputPortAdapter: OfferDatastoreOutputPortAdapter

    @Test
    fun `Should create an Offer with Customizations and Options successfully`() {
        val storeId = Id()
        val category = mockCategory()
        val offer = mockOfferWith {
            this.customizations = mutableListOf(mockCustomization())
        }

        offer.getAllProducts().forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)
        offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)

        val result = offerDatastoreOutputPortAdapter.findById(storeId, offer.id)

        result.shouldNotBeNull()
        result shouldBeEqual offer
    }

    @Test
    fun `Should return null when offer not exists`() {
        val result = offerDatastoreOutputPortAdapter.findById(Id(), Id())

        result.shouldBeNull()
    }

    @Test
    fun `Should return true when the Offer exists by Offer Id`() {
        val storeId = Id()
        val category = mockCategory()
        val product = mockProduct()
        val offer = mockOfferWith {
            this.product = product
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)
        productDatastoreOutputPortAdapter.create(storeId, product)
        offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)

        val result = offerDatastoreOutputPortAdapter.exists(offer.id)

        result shouldBe true
    }

    @Test
    fun `Should return false when the Offer doesn't exists by Offer Id`() {
        val result = offerDatastoreOutputPortAdapter.exists(Id())

        result shouldBe false
    }

    @Test
    fun `Should return true when the Offer exists by Offer Id and Store Id`() {
        val storeId = Id()
        val category = mockCategory()
        val product = mockProduct()
        val offer = mockOfferWith {
            this.product = product
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)
        productDatastoreOutputPortAdapter.create(storeId, product)
        offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)

        val result = offerDatastoreOutputPortAdapter.exists(storeId, offer.id)

        result shouldBe true
    }

    @Test
    fun `Should return false when the Offer doesn't exists by Offer Id and Store Id`() {
        val result = offerDatastoreOutputPortAdapter.exists(Id(), Id())

        result shouldBe false
    }

    @Test
    fun `Should delete Offer successfully`() {
        val storeId = Id()
        val category = mockCategory()
        val product = mockProduct()
        val offer = mockOfferWith {
            this.product = product
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)
        productDatastoreOutputPortAdapter.create(storeId, product)
        offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)

        offerDatastoreOutputPortAdapter.delete(storeId, offer.id)

        val result = offerDatastoreOutputPortAdapter.exists(storeId, offer.id)

        result shouldBe false
    }

    @Test
    fun `Should update an Offer successfully`() {
        val storeId = Id()
        val category = mockCategory()
        val offer = mockOffer()

        offer.getAllProducts().forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)
        offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)

        val updateOffer = mockOfferWith {
            id = offer.id
        }

        updateOffer.getAllProducts().forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        offerDatastoreOutputPortAdapter.update(storeId, updateOffer)

        val result = offerDatastoreOutputPortAdapter.findById(storeId, offer.id)

        result.shouldNotBeNull()
        result shouldBeEqual updateOffer
    }

    @Test
    fun `Should update an Offer with Customizations and Options successfully`() {
        val storeId = Id()
        val category = mockCategory()
        val offer = mockOffer()

        offer.getAllProducts().forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)
        offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)

        val option = mockOptionWith {
            customizations = mutableListOf(mockCustomizationWith { description = null })
        }

        val customizations = mockCustomizationWith {
            options = mutableListOf(option)
        }

        val updateOffer = mockOfferWith {
            id = offer.id
            this.customizations = mutableListOf(customizations)
        }

        updateOffer.getAllProducts().forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        offerDatastoreOutputPortAdapter.update(storeId, updateOffer)

        val result = offerDatastoreOutputPortAdapter.findById(storeId, offer.id)

        result.shouldNotBeNull()
        result shouldBeEqual updateOffer
    }

    @Test
    fun `Should count 10 Offers successfully`() {
        val storeId = Id()
        val category = mockCategory()
        val offers = List(10) {
            mockOffer()
        }

        offers.flatMap { offer -> offer.getAllProducts() }.forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)

        offers.forEach { offer ->
            offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)
        }

        val result = offerDatastoreOutputPortAdapter.countOffers(storeId, category.id, null)

        result shouldBe 10
    }

    @Test
    fun `Should count 10 Offers when call with begins with YES`() {
        val storeId = Id()
        val category = mockCategory()
        val offersYes = List(10) {
            mockOfferWith {
                name = Name("YES" + randomString(30))
            }
        }
        val offers = List(10) {
            mockOfferWith {
                name = Name("NOPE" + randomString(30))
            }
        } + offersYes

        offers.flatMap { offer -> offer.getAllProducts() }.forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)

        offers.forEach { offer ->
            offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)
        }

        val result = offerDatastoreOutputPortAdapter.countOffers(storeId, category.id, "YES")

        result shouldBe 10
    }

    @Test
    fun `Should get 10 offers successfully`() {
        val storeId = Id()
        val category = mockCategory()
        val offers = List(10) {
            mockOffer()
        }

        offers.flatMap { offer -> offer.getAllProducts() }.forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)

        offers.forEach { offer ->
            offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)
        }

        val result = offerDatastoreOutputPortAdapter.getOffers(storeId, category.id, 20, 0, null)

        result.toSet() shouldBeEqual offers.toSet()
    }

    @Test
    fun `Should get 10 Offers with Customizations and Options successfully`() {
        val storeId = Id()
        val category = mockCategory()
        val offers = List(10) {
            mockOfferWith {
                customizations = mutableListOf(mockCustomization())
            }
        }

        offers.flatMap { offer -> offer.getAllProducts() }.forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)

        offers.forEach { offer ->
            offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)
        }

        val result = offerDatastoreOutputPortAdapter.getOffers(storeId, category.id, 20, 0, "  ")

        result.toSet() shouldBeEqual offers.toSet()
    }

    @Test
    fun `Should get 10 Offers when call with begins with YES`() {
        val storeId = Id()
        val category = mockCategory()
        val offersYes = List(10) {
            mockOfferWith {
                name = Name("YES" + randomString(30))
            }
        }
        val offers = List(10) {
            mockOffer()
        } + offersYes

        offers.flatMap { offer -> offer.getAllProducts() }.forEach { product ->
            productDatastoreOutputPortAdapter.create(storeId, product)
        }

        categoryDatastoreOutputPortAdapter.create(storeId, category)

        offers.forEach { offer ->
            offerDatastoreOutputPortAdapter.create(storeId, category.id, offer)
        }

        val result = offerDatastoreOutputPortAdapter.getOffers(storeId, category.id, 20, 0, " YES  ")

        result.toSet() shouldBeEqual offersYes.toSet()
    }
}
