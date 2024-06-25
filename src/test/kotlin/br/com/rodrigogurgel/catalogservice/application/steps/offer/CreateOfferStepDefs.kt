package br.com.rodrigogurgel.catalogservice.application.steps.offer

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.OfferContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.offer.CreateOfferInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.verifySequence

class CreateOfferStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val productContext: ProductContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
    private val offerContext: OfferContextStepDefs,
) {
    private val createOfferUseCase = CreateOfferInputPort(
        storeContext.storeDatastoreOutputPort,
        categoryContext.categoryDatastoreOutputPort,
        productContext.productDatastoreOutputPort,
        offerContext.offerDatastoreOutputPort
    )

    private lateinit var offerWithNewProductName: Offer

    @Before
    fun setUp() {
        every {
            offerContext.offerDatastoreOutputPort.create(any(), any(), any())
        } returns Unit

        every {
            productContext.productDatastoreOutputPort.getIfNotExists(
                any(),
                match { it == listOf(Id("92ddeebf-da50-402f-b850-19e5fb093a0a")) }
            )
        } returns listOf(Id("92ddeebf-da50-402f-b850-19e5fb093a0a"))
    }

    @When("I create a offer into store")
    fun iCreateAOfferIntoStore() {
        createOfferUseCase.execute(storeContext.store.id, categoryContext.category.id, offerContext.offer)
    }

    @When("I create a offer into store with this product")
    fun iCreateAOfferIntoStoreWithThisProduct() {
        createOfferUseCase.execute(storeContext.store.id, categoryContext.category.id, offerWithNewProductName)
    }

    @Then("the offer should be persist in the datastore")
    fun theOfferShouldBePersistInTheDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            categoryContext.categoryDatastoreOutputPort.exists(storeContext.store.id, categoryContext.category.id)
            productContext.productDatastoreOutputPort.getIfNotExists(
                storeContext.store.id,
                listOf(productContext.product.id)
            )
            offerContext.offerDatastoreOutputPort.create(
                storeContext.store.id,
                categoryContext.category.id,
                offerContext.offer
            )
        }
    }

    @When("I change product name to {string}")
    fun iChangeProductNameTo(newProductName: String) {
        val product = offerContext.offer.product.copy(name = Name(newProductName))
        offerWithNewProductName = offerContext.offer.copy(product = product)
    }

    @Then("the offer with new product name should be persist in the datastore")
    fun theOfferWithNewProductNameShouldBePersistInTheDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            categoryContext.categoryDatastoreOutputPort.exists(storeContext.store.id, categoryContext.category.id)
            productContext.productDatastoreOutputPort.getIfNotExists(
                storeContext.store.id,
                listOf(productContext.product.id)
            )
            offerContext.offerDatastoreOutputPort.create(
                storeContext.store.id,
                categoryContext.category.id,
                offerWithNewProductName
            )
        }
    }

    @Then("the product shouldn't be updated in the datastore")
    fun theProductShouldnTBeUpdatedInTheDatastore() {
        productContext.product.name shouldNotBe offerWithNewProductName.product.name
    }

    @When("I try create a offer into store with this id {string}")
    fun iTryCreateAOfferIntoStoreWithThisId(storeId: String) {
        val exception = shouldThrow<StoreNotFoundException> {
            createOfferUseCase.execute(Id(storeId), categoryContext.category.id, offerContext.offer)
        }

        exception.message shouldContain storeId
    }

    @When("I try create a offer into category with this id {string}")
    fun iTryCreateAOfferIntoCategoryWithThisId(categoryId: String) {
        val exception = shouldThrow<CategoryNotFoundException> {
            createOfferUseCase.execute(storeContext.store.id, Id(categoryId), offerContext.offer)
        }

        exception.message shouldContain categoryId
    }

    @When("I try create a offer with product that has this id {string}")
    fun iTryCreateAOfferWithProductThatHasThisId(productId: String) {
        val exception = shouldThrow<ProductsNotFoundException> {
            createOfferUseCase.execute(
                storeContext.store.id,
                categoryContext.category.id,
                mockOfferWith {
                    product = mockProductWith {
                        id = Id(productId)
                    }
                }
            )
        }

        exception.message shouldContain productId
    }
}
