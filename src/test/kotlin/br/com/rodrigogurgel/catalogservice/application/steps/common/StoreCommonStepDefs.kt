package br.com.rodrigogurgel.catalogservice.application.steps.common

import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.domain.entity.Store
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.mockk.every

class StoreCommonStepDefs(
    private val storeContext: StoreContextStepDefs,
) {
    @Given("the following store exists:")
    fun theFollowingStoreExists(store: Store) {
        storeContext.store = store
        every { storeContext.storeDatastoreOutputPort.exists(store.id) } returns true
    }

    @Then("I get an error")
    fun iGetAnError() = Unit
}
