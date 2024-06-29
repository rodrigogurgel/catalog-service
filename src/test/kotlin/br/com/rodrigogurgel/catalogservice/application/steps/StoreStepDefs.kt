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
    private val cucumberContext: CucumberContext
) {
    @And("that there is a Store with the Id {string}")
    fun thatThereIsAStoreWithTheId(storeId: String) {
        every { cucumberContext.storeDatastoreOutputPort.exists(Id(UUID.fromString(storeId))) } returns true
    }

    @And("that there isn't a Store with the Id {string}")
    fun thatThereIsnTAStoreWithTheId(storeId: String) {
        every { cucumberContext.storeDatastoreOutputPort.exists(Id(UUID.fromString(storeId))) } returns false
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
