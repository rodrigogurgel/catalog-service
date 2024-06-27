package br.com.rodrigogurgel.catalogservice.application.steps.common

import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.domain.entity.Store
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.mockk.every

class StoreCommonStepDefs(
    private val storeContext: StoreContextStepDefs,
) {
    @Given("a id {string} with no store associated")
    fun aIdWithNoStoreAssociated(id: String) {
        every { storeContext.storeDatastoreOutputPort.exists(Id(id)) } returns false
    }

    @Given("an id {string} with no store associated")
    fun anIdWithNoStoreAssociated(id: String) {
        every { storeContext.storeDatastoreOutputPort.exists(Id(id)) } returns false
    }

    @Given("the following store exists:")
    fun theFollowingStoreExists(store: Store) {
        storeContext.store = store
        every { storeContext.storeDatastoreOutputPort.exists(store.id) } returns true
    }

    @Given("the following store information:")
    fun theFollowingStoreInformation(store: Store) {
        storeContext.store = store
        every { storeContext.storeDatastoreOutputPort.exists(store.id) } returns false
        every { storeContext.storeDatastoreOutputPort.create(store) } returns Unit
    }

    @Then("I get an error")
    fun iGetAnError() = Unit
}
