package br.com.rodrigogurgel.catalogservice.application.steps.category

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.category.CreateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CreateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.verifySequence

class CreateCategoryStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
) {
    private val createCategoryUseCase: CreateCategoryUseCase =
        CreateCategoryInputPort(
            storeContext.storeDatastoreOutputPort,
            categoryContext.categoryDatastoreOutputPort
        )

    @Before
    fun setUp() {
        every { categoryContext.categoryDatastoreOutputPort.create(any(), any()) } returns Unit
    }

    @When("I add a category into store")
    fun iAddACategoryIntoStore() {
        createCategoryUseCase.execute(storeContext.store.id, categoryContext.category)
    }

    @Then("the category should be persist in the datastore")
    fun theCategoryShouldBePersistInTheStore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            categoryContext.categoryDatastoreOutputPort.create(storeContext.store.id, categoryContext.category)
        }
    }

    @When("I try add a category into store with this id {string}")
    fun iTryAddACategoryIntoStoreWithThisId(id: String) {
        val exception = shouldThrow<StoreNotFoundException> {
            createCategoryUseCase.execute(Id(id), categoryContext.category)
        }

        exception.message shouldContain id
    }
}
