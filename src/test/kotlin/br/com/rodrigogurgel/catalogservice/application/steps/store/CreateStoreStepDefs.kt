package br.com.rodrigogurgel.catalogservice.application.steps.store

import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.StoreAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.application.port.input.store.CreateStoreInputPort
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifySequence

class CreateStoreStepDefs(
    private val storeContext: StoreContextStepDefs,
) {
    private val createStoreUseCase = spyk(
        CreateStoreInputPort(
            storeContext.storeDatastoreOutputPort
        )
    )

    @When("I create a store")
    fun iCreateAStore() {
        createStoreUseCase.execute(storeContext.store)
    }

    @Then("the store should be persisted in the datastore")
    fun theStoreShouldBePersistedInTheDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            storeContext.storeDatastoreOutputPort.create(storeContext.store)
        }
    }

    @When("I try to create a store with the id {string} that already exists")
    fun iTryToCreateAStoreWithTheIdThatAlreadyExists(storeId: String) {
        val exception = shouldThrow<StoreAlreadyExistsException> {
            createStoreUseCase.execute(storeContext.store)
        }

        exception.message shouldContain storeId
    }

    @Then("I should get an error StoreAlreadyExistsException")
    fun iShouldGetAnErrorStoreAlreadyExistsException() {
        verify { createStoreUseCase.execute(storeContext.store) }
        confirmVerified(createStoreUseCase)
    }
}
