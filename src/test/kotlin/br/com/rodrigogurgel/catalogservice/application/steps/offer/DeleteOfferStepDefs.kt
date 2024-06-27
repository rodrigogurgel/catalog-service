package br.com.rodrigogurgel.catalogservice.application.steps.offer

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.OfferContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.DeleteOfferInputPort
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain

class DeleteOfferStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
    private val productContext: ProductContextStepDefs,
    private val offerContext: OfferContextStepDefs,
) {
    private val deleteOfferUseCase = DeleteOfferInputPort(
        storeContext.storeDatastoreOutputPort,
        categoryContext.categoryDatastoreOutputPort,
        offerContext.offerDatastoreOutputPort
    )

    @When("I delete a offer with id {string} from store with id {string} and category id {string}")
    fun iDeleteAOfferWithIdFromStoreWithIdAndCategoryId(offerId: String, storeId: String, categoryId: String) {
        deleteOfferUseCase.execute(Id(offerId), Id(storeId), Id(categoryId))
    }

    @Then("the offer should be remove from datastore")
    fun theOfferShouldBeRemoveFromDatastore() {
        storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
        categoryContext.categoryDatastoreOutputPort.exists(storeContext.store.id, categoryContext.category.id)
        productContext.productDatastoreOutputPort.getIfNotExists(
            storeContext.store.id,
            listOf(productContext.product.id)
        )
        offerContext.offerDatastoreOutputPort.delete(
            storeContext.store.id,
            categoryContext.category.id,
            offerContext.offer.id
        )
    }

    @When("I try delete a offer from store with this id {string}")
    fun iTryDeleteAOfferFromStoreWithThisId(storeId: String) {
        val exception = shouldThrow<StoreNotFoundException> {
            deleteOfferUseCase.execute(Id(storeId), categoryContext.category.id, offerContext.offer.id)
        }

        exception.message shouldContain storeId
    }

    @When("I try delete a offer from category with this id {string}")
    fun iTryDeleteAOfferFromCategoryWithThisId(categoryId: String) {
        val exception = shouldThrow<CategoryNotFoundException> {
            deleteOfferUseCase.execute(storeContext.store.id, Id(categoryId), offerContext.offer.id)
        }

        exception.message shouldContain categoryId
    }
}
