package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.CucumberContext
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.And
import io.cucumber.java.en.Then
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import java.util.UUID

class StoreStepDefs(
    private val cucumberContext: CucumberContext,
) {
    @And("that there is a Store with the Id {string}")
    fun thatThereIsAStoreWithTheId(storeIdString: String) {
        val storeId = Id(UUID.fromString(storeIdString))
        every { cucumberContext.storeOutputPort.exists(storeId) } returns true

        every {
            cucumberContext.productOutputPort.getProducts(storeId, any(), any(), any())
        } returns emptyList()

        every {
            cucumberContext.productOutputPort.countProducts(storeId, any())
        } returns 0

        every {
            cucumberContext.categoryOutputPort.getCategories(storeId, any(), any(), any())
        } returns emptyList()

        every {
            cucumberContext.categoryOutputPort.countCategories(storeId, any())
        } returns 0
    }

    @And("that there isn't a Store with the Id {string}")
    fun thatThereIsnTAStoreWithTheId(storeIdString: String) {
        val storeId = Id(UUID.fromString(storeIdString))

        every { cucumberContext.storeOutputPort.exists(storeId) } returns false
    }

    @And("the Id of the Store is {string}")
    fun theIdOfTheStoreIs(storeId: String) {
        cucumberContext.storeId = Id(UUID.fromString(storeId))
    }

    @Then("I should encounter a {string} error")
    fun iShouldEncounterAError(exceptionClassName: String) {
        cucumberContext.result.isFailure shouldBe true
        cucumberContext.result.exceptionOrNull() shouldNotBe null
        cucumberContext.result.exceptionOrNull()!!::class.simpleName shouldBe exceptionClassName
    }
}
