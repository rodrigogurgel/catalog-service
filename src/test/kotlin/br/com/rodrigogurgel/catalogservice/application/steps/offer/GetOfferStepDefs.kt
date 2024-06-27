package br.com.rodrigogurgel.catalogservice.application.steps.offer

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.OfferContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.OfferNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.GetOfferInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.verifySequence

class GetOfferStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
    private val offerContext: OfferContextStepDefs,
) {

    private lateinit var offerRetrieve: Offer

    private val getOfferUseCase = GetOfferInputPort(
        storeContext.storeDatastoreOutputPort,
        categoryContext.categoryDatastoreOutputPort,
        offerContext.offerDatastoreOutputPort
    )

    @When("I get a offer with id {string} from store with id {string} and category id {string}")
    fun iGetAOfferWithIdFromStoreWithIdAndCategoryId(offerId: String, storeId: String, categoryId: String) {
        offerRetrieve = getOfferUseCase.execute(Id(storeId), Id(categoryId), Id(offerId))
    }

    @Then("the offer should have same information")
    fun theOfferShouldHaveSameInformation() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            categoryContext.categoryDatastoreOutputPort.exists(storeContext.store.id, categoryContext.category.id)
            offerContext.offerDatastoreOutputPort.findById(
                storeContext.store.id,
                categoryContext.category.id,
                offerContext.offer.id
            )
        }

        offerRetrieve shouldBe offerContext.offer
    }

    @When("I try get a offer from store with this id {string}")
    fun iTryGetAOfferFromStoreWithThisId(storeId: String) {
        val exception = shouldThrow<StoreNotFoundException> {
            getOfferUseCase.execute(Id(storeId), categoryContext.category.id, offerContext.offer.id)
        }

        exception.message shouldContain storeId
    }

    @When("I try get a offer from category with this id {string}")
    fun iTryGetAOfferFromCategoryWithThisId(categoryId: String) {
        val exception = shouldThrow<CategoryNotFoundException> {
            getOfferUseCase.execute(storeContext.store.id, Id(categoryId), offerContext.offer.id)
        }

        exception.message shouldContain categoryId
    }

    @When("I try get a offer with this id {string}")
    fun iTryGetAOfferWithThisId(offerId: String) {
        val exception = shouldThrow<OfferNotFoundException> {
            getOfferUseCase.execute(storeContext.store.id, categoryContext.category.id, Id(offerId))
        }

        exception.message shouldContain offerId
    }
}
