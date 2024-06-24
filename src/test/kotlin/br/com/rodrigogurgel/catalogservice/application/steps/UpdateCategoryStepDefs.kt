package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.category.UpdateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategoryWith
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.every
import io.mockk.verifySequence
import org.junit.jupiter.api.assertThrows

class UpdateCategoryStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
) {
    private val updateCategoryUseCase =
        UpdateCategoryInputPort(storeContext.storeDatastoreOutputPort, categoryContext.categoryDatastoreOutputPort)

    @Before
    fun setUp() {
        every {
            categoryContext.categoryDatastoreOutputPort.exists(
                any(),
                match { it == Id("92ddeebf-da50-402f-b850-19e5fb093a0a") }
            )
        } throws CategoryNotFoundException(Id("92ddeebf-da50-402f-b850-19e5fb093a0a"))
    }

    @When("I update category name to {string} into store")
    fun iUpdateCategoryNameToIntoStore(newCategoryName: String) {
        updateCategoryUseCase.execute(
            storeContext.store.id,
            categoryContext.category.copy(
                name = Name(newCategoryName)
            )
        )
    }

    @When("I try update a category with this id {string} from store")
    fun iTryUpdateACategoryWithThisIdFromStore(categoryId: String) {
        assertThrows<CategoryNotFoundException> {
            updateCategoryUseCase.execute(
                storeContext.store.id,
                mockCategoryWith {
                    id = Id(categoryId)
                }
            )
        }
    }

    @Then("the category with new information's should be persist in the datastore")
    fun theCategoryWithNewInformationSShouldBePersistInTheDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            categoryContext.categoryDatastoreOutputPort.exists(storeContext.store.id, any())
            categoryContext.categoryDatastoreOutputPort.update(
                storeContext.store.id,
                match { categoryContext.category.id == it.id }
            )
        }
    }
}
