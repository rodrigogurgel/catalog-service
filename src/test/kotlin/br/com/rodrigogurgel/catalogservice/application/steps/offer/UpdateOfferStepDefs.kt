package br.com.rodrigogurgel.catalogservice.application.steps.offer

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.OfferContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.ProductsNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.UpdateOfferInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain

class UpdateOfferStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
    private val productContext: ProductContextStepDefs,
    private val offerContext: OfferContextStepDefs,
) {
    private lateinit var updatedOffer: Offer

    private val updateOfferUseCase = UpdateOfferInputPort(
        storeContext.storeDatastoreOutputPort,
        categoryContext.categoryDatastoreOutputPort,
        productContext.productDatastoreOutputPort,
        offerContext.offerDatastoreOutputPort
    )

    @When("I update offer price to {string} into store")
    fun iUpdateOfferPriceToIntoStore(newPrice: String) {
        val price = Price(newPrice.toBigDecimal())
        updatedOffer = offerContext.offer.run { Offer(id, product, price, status, customizations) }
        updateOfferUseCase.execute(
            storeContext.store.id,
            categoryContext.category.id,
            updatedOffer
        )
    }

    @Then("the offer with new information should be persist in the datastore")
    fun theOfferWithNewInformationShouldBePersistInTheDatastore() {
        storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
        categoryContext.categoryDatastoreOutputPort.exists(storeContext.store.id, categoryContext.category.id)
        productContext.productDatastoreOutputPort.getIfNotExists(
            storeContext.store.id,
            listOf(productContext.product.id)
        )
        offerContext.offerDatastoreOutputPort.update(
            storeContext.store.id,
            categoryContext.category.id,
            updatedOffer
        )
    }

    @When("I try update a offer into store with this id {string}")
    fun iTryUpdateAOfferIntoStoreWithThisId(storeId: String) {
        val exception = shouldThrow<StoreNotFoundException> {
            updateOfferUseCase.execute(
                Id(storeId),
                categoryContext.category.id,
                offerContext.offer
            )
        }

        exception.message shouldContain storeId
    }

    @When("I try update a offer product to a product with this id {string}")
    fun iTryUpdateAOfferProductToAProductWithThisId(productId: String) {
        val product = mockProductWith { id = Id(productId) }
        val exception = shouldThrow<ProductsNotFoundException> {
            updateOfferUseCase.execute(
                storeContext.store.id,
                categoryContext.category.id,
                offerContext.offer.run { Offer(id, product, price, status, customizations) }
            )
        }

        exception.message shouldContain productId
    }

    @When("I try update a offer with this id {string} from store")
    fun iTryUpdateAOfferWithThisIdFromStore(offerId: String) {
        val id = Id(offerId)
        val exception = shouldThrow<OfferNotFoundException> {
            updateOfferUseCase.execute(
                storeContext.store.id,
                categoryContext.category.id,
                offerContext.offer.run { Offer(id, product, price, status, customizations) }
            )
        }

        exception.message shouldContain offerId
    }

    @When("I try update a offer into category with this id {string}")
    fun iTryUpdateAOfferIntoCategoryWithThisId(categoryId: String) {
        val exception = shouldThrow<CategoryNotFoundException> {
            updateOfferUseCase.execute(
                storeContext.store.id,
                Id(categoryId),
                offerContext.offer
            )
        }

        exception.message shouldContain categoryId
    }
}
