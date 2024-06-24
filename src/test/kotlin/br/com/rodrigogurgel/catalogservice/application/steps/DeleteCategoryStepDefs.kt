package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.category.DeleteCategoryInputPort
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.verifySequence

class DeleteCategoryStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
) {
    private val deleteCategoryUseCase =
        DeleteCategoryInputPort(storeContext.storeDatastoreOutputPort, categoryContext.categoryDatastoreOutputPort)

    @Before
    fun setUp() {
        every {
            categoryContext.categoryDatastoreOutputPort.delete(any(), any())
        } returns Unit
    }

    @When("I delete a category from store")
    fun iDeleteACategoryFromStore() {
        deleteCategoryUseCase.execute(storeContext.store.id, categoryContext.category.id)
    }

    @Then("the category should be remove from datastore")
    fun theCategoryShouldBeRemoveFromDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            categoryContext.categoryDatastoreOutputPort.delete(storeContext.store.id, categoryContext.category.id)
        }
    }

    @When("I try remove a category from store with this id {string}")
    fun iTryRemoveACategoryFromStoreWithThisId(id: String) {
        shouldThrow<StoreNotFoundException> {
            deleteCategoryUseCase.execute(Id(id), categoryContext.category.id)
        }
    }
}
