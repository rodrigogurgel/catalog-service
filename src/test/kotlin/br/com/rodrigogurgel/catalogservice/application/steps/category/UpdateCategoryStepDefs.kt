package br.com.rodrigogurgel.catalogservice.application.steps.category

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.category.UpdateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategoryWith
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.verifySequence

class UpdateCategoryStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
) {
    private lateinit var updatedCategory: Category

    private val updateCategoryUseCase =
        UpdateCategoryInputPort(storeContext.storeDatastoreOutputPort, categoryContext.categoryDatastoreOutputPort)

    @Before
    fun setUp() {
        every {
            categoryContext.categoryDatastoreOutputPort.exists(
                any(),
                match { it == Id("92ddeebf-da50-402f-b850-19e5fb093a0a") }
            )
        } returns false
    }

    @When("I update category name to {string} into store")
    fun iUpdateCategoryNameToIntoStore(newCategoryName: String) {
        updatedCategory = categoryContext.category.copy(
            name = Name(newCategoryName)
        )
        updateCategoryUseCase.execute(
            storeContext.store.id,
            updatedCategory
        )
    }

    @When("I try update a category with this id {string} from store")
    fun iTryUpdateACategoryWithThisIdFromStore(categoryId: String) {
        val exception = shouldThrow<CategoryNotFoundException> {
            updateCategoryUseCase.execute(
                storeContext.store.id,
                mockCategoryWith {
                    id = Id(categoryId)
                }
            )
        }

        exception.message shouldContain categoryId
    }

    @When("I try update a category into store with this id {string}")
    fun iTryUpdateACategoryIntoStoreWithThisId(storeId: String) {
        val exception = shouldThrow<StoreNotFoundException> {
            updateCategoryUseCase.execute(
                Id(storeId),
                categoryContext.category
            )
        }

        exception.message shouldContain storeId
    }

    @Then("the category with new information should be persist in the datastore")
    fun theCategoryWithNewInformationShouldBePersistInTheDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            categoryContext.categoryDatastoreOutputPort.exists(storeContext.store.id, any())
            categoryContext.categoryDatastoreOutputPort.update(
                storeContext.store.id,
                updatedCategory
            )
        }
    }
}
