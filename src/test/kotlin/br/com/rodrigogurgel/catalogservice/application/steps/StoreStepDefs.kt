package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.CucumberContext
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import java.util.UUID

class StoreStepDefs(
    private val cucumberContext: CucumberContext,
) {
    @And("that there is a Store with the Id {string}")
    fun thatThereIsAStoreWithTheId(storeIdString: String) {
        val storeId = Id(UUID.fromString(storeIdString))
        every { cucumberContext.storeDatastoreOutputPort.exists(storeId) } returns true

        every {
            cucumberContext.productDatastoreOutputPort.getProducts(storeId, any(), any(), any())
        } returns emptyList()

        every {
            cucumberContext.productDatastoreOutputPort.countProducts(storeId, any())
        } returns 0

        every {
            cucumberContext.categoryDatastoreOutputPort.getCategories(storeId, any(), any(), any())
        } returns emptyList()

        every {
            cucumberContext.categoryDatastoreOutputPort.countCategories(storeId, any())
        } returns 0

        every {
            cucumberContext.offerDatastoreOutputPort.getOffers(storeId, any(), any(), any(), any())
        } returns emptyList()

        every {
            cucumberContext.offerDatastoreOutputPort.countOffers(storeId, any(), any())
        } returns 0
    }

    @And("that there isn't a Store with the Id {string}")
    fun thatThereIsnTAStoreWithTheId(storeIdString: String) {
        val storeId = Id(UUID.fromString(storeIdString))

        every { cucumberContext.storeDatastoreOutputPort.exists(storeId) } returns false
    }

    @And("the Id of the Store is {string}")
    fun theIdOfTheStoreIs(storeId: String) {
        cucumberContext.storeId = Id(UUID.fromString(storeId))
    }

    @Then("I should encounter a {string} error")
    fun iShouldEncounterAError(exceptionClassName: String) {
        cucumberContext.result.isFailure shouldBe true
        cucumberContext.result.exceptionOrNull().shouldNotBeNull()
        cucumberContext.result.exceptionOrNull()!!::class.simpleName shouldBe exceptionClassName
    }
}
