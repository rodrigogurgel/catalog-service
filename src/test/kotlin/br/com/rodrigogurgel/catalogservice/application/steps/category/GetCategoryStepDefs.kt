package br.com.rodrigogurgel.catalogservice.application.steps.category

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.CategoryNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.category.GetCategoryInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class GetCategoryStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
) {
    private lateinit var categoryRetrieve: Category

    private val getCategoryUseCase =
        GetCategoryInputPort(storeContext.storeDatastoreOutputPort, categoryContext.categoryDatastoreOutputPort)

    @When("I get a category with id {string} from store with id {string}")
    fun iGetACategoryWithIdFromStoreWithId(categoryId: String, storeId: String) {
        categoryRetrieve = getCategoryUseCase.execute(Id(storeId), Id(categoryId))
    }

    @Then("the category should have same information")
    fun theCategoryShouldHaveSameInformation() {
        categoryRetrieve shouldBe categoryContext.category
    }

    @When("I try get a category from store with this id {string}")
    fun iTryGetACategoryFromStoreWithThisId(storeId: String) {
        shouldThrow<StoreNotFoundException> {
            getCategoryUseCase.execute(Id(storeId), categoryContext.category.id)
        }
    }

    @When("I try get a category with this id {string} from store")
    fun iTryGetACategoryWithThisIdFromStore(categoryId: String) {
        val exception = shouldThrow<CategoryNotFoundException> {
            getCategoryUseCase.execute(storeContext.store.id, Id(categoryId))
        }

        exception.message shouldContain categoryId
    }
}
