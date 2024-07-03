package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.CucumberContext
import br.com.rodrigogurgel.catalogservice.application.port.input.category.CountCategoriesInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.CreateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.DeleteCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.GetCategoriesInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.GetCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.UpdateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategoryWith
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.verifySequence
import java.util.UUID

class CategoryStepDefs(private val cucumberContext: CucumberContext) {
    private lateinit var category: Category

    private val createCategoryInputPort = CreateCategoryInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.categoryOutputPort
    )

    private val deleteCategoryInputPort = DeleteCategoryInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.categoryOutputPort
    )

    private val getCategoryInputPort = GetCategoryInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.categoryOutputPort
    )

    private val updateCategoryInputPort = UpdateCategoryInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.categoryOutputPort
    )

    private val getCategoriesInputPort = GetCategoriesInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.categoryOutputPort
    )

    private val countCategoriesInputPort = CountCategoriesInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.categoryOutputPort
    )

    @Given("the information of the Category")
    fun theInformationOfTheCategory(category: Category) {
        this.category = category
    }

    @When("I attempt to create a Category")
    fun iAttemptToCreateACategory() {
        cucumberContext.result = runCatching {
            createCategoryInputPort.execute(cucumberContext.storeId, category)
        }
    }

    @Then("the Category should be stored in the database")
    fun theCategoryShouldBeStoredInTheDatabase() {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)
            cucumberContext.categoryOutputPort.exists(
                category.id
            )
            cucumberContext.categoryOutputPort.create(cucumberContext.storeId, category)
        }
    }

    @When("I attempt to create a Category using the Id {string}")
    fun iAttemptToCreateACategoryUsingTheId(categoryIdString: String) {
        cucumberContext.result = runCatching {
            createCategoryInputPort.execute(
                cucumberContext.storeId,
                category.copy(
                    id = Id(UUID.fromString(categoryIdString))
                )
            )
        }
    }

    @When("I attempt to delete a Category with the Id {string}")
    fun iAttemptToDeleteACategoryWithTheId(categoryIdString: String) {
        cucumberContext.result = runCatching {
            deleteCategoryInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(categoryIdString)))
        }
    }

    @Then("the Category with the Id {string} should be deleted from database")
    fun theCategoryWithTheIdShouldBeDeletedFromDatabase(categoryIdString: String) {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)

            cucumberContext.categoryOutputPort.delete(
                cucumberContext.storeId,
                match { id -> id == Id(UUID.fromString(categoryIdString)) }
            )
        }
    }

    @When("I attempt to get a Category with the Id {string}")
    fun iAttemptToGetACategoryWithTheId(categoryIdString: String) {
        cucumberContext.result = runCatching {
            getCategoryInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(categoryIdString)))
        }
    }

    @And("that there isn't a Category with the Id {string}")
    fun thatThereIsnTACategoryWithTheId(categoryIdString: String) {
        val id = Id(UUID.fromString(categoryIdString))

        every {
            cucumberContext.categoryOutputPort.exists(
                id
            )
        } returns false

        every {
            cucumberContext.categoryOutputPort.exists(
                any(),
                id
            )
        } returns false

        justRun {
            cucumberContext.categoryOutputPort.create(
                any(),
                match { category -> category.id == id }
            )
        }

        every {
            cucumberContext.categoryOutputPort.findById(
                any(),
                id
            )
        } returns null
    }

    @When("I attempt to update a Category with the Id {string}")
    fun iAttemptToUpdateACategoryWithTheId(categoryIdString: String) {
        cucumberContext.result = runCatching {
            updateCategoryInputPort.execute(
                cucumberContext.storeId,
                category.copy(id = Id(UUID.fromString(categoryIdString)))
            )
        }
    }

    @Then("the Category with the Id {string} should be retrieved from database")
    fun theCategoryWithTheIdShouldBeRetrievedFromDatabase(categoryIdString: String) {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)

            cucumberContext.categoryOutputPort.findById(
                cucumberContext.storeId,
                match { id -> id == Id(UUID.fromString(categoryIdString)) }
            )
        }
    }

    @Then("the Category should be updated in the database")
    fun theCategoryShouldBeUpdatedInTheDatabase() {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)

            cucumberContext.categoryOutputPort.exists(
                cucumberContext.storeId,
                category.id
            )

            cucumberContext.categoryOutputPort.update(
                cucumberContext.storeId,
                category
            )
        }
    }

    @And("that there is a Category with the Id {string} in the Store with the Id {string}")
    fun thatThereIsACategoryWithTheIdInTheStoreWithTheId(categoryIdString: String, storeIdString: String) {
        val storeId = Id(UUID.fromString(storeIdString))
        val categoryId = Id(UUID.fromString(categoryIdString))
        val category = mockCategoryWith { id = categoryId }
        every { cucumberContext.categoryOutputPort.exists(categoryId) } returns true

        every {
            cucumberContext.categoryOutputPort.exists(
                storeId,
                categoryId
            )
        } returns true

        every {
            cucumberContext.categoryOutputPort.findById(
                storeId,
                categoryId
            )
        } returns category

        justRun {
            cucumberContext.categoryOutputPort.delete(
                storeId,
                categoryId
            )
        }

        justRun {
            cucumberContext.categoryOutputPort.update(
                storeId,
                match { category -> category.id == categoryId }
            )
        }
    }

    @When("I attempt to update a Category")
    fun iAttemptToUpdateACategory() {
        cucumberContext.result = runCatching {
            updateCategoryInputPort.execute(
                cucumberContext.storeId,
                category
            )
        }
    }

    @When("I attempt to update a Category using the Id {string}")
    fun iAttemptToUpdateACategoryUsingTheId(categoryIdString: String) {
        cucumberContext.result = runCatching {
            updateCategoryInputPort.execute(
                cucumberContext.storeId,
                category.copy(id = Id(UUID.fromString(categoryIdString)))
            )
        }
    }

    @When("I attempt to get a Categories with the limit as {string}, offset as {string} and begins with as {string}")
    fun iAttemptToGetACategoriesWithTheLimitAsOffsetAsAndBeginsWithAs(
        limit: String,
        offset: String,
        beginsWith: String,
    ) {
        cucumberContext.result = runCatching {
            getCategoriesInputPort.execute(cucumberContext.storeId, limit.toInt(), offset.toInt(), beginsWith)
        }.onFailure { it.printStackTrace() }
    }

    @Then(
        "the Categories should be retrieved from database with the limit as {string}, offset as {string} and begins with as {string}"
    )
    fun theCategoriesShouldBeRetrievedFromDatabaseWithTheLimitAsOffsetAsAndBeginsWithAs(
        limit: String,
        offset: String,
        beginsWith: String,
    ) {
        cucumberContext.result.isSuccess shouldBe true
        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)
            cucumberContext.categoryOutputPort.getCategories(
                cucumberContext.storeId,
                limit.toInt(),
                offset.toInt(),
                beginsWith
            )
        }
    }

    @When(
        "I attempt to count the Categories with the begins with as {string}"
    )
    fun iAttemptToCountTheCategoriesWithTheLimitAsOffsetAsAndBeginsWithAs(beginsWith: String) {
        cucumberContext.result = runCatching {
            countCategoriesInputPort.execute(cucumberContext.storeId, beginsWith)
        }.onFailure { it.printStackTrace() }
    }

    @Then(
        "the Categories should be counted in the database with the begins with as {string}"
    )
    fun theCategoriesShouldBeCountedInTheDatabaseWithTheLimitAsOffsetAsAndBeginsWithAs(beginsWith: String) {
        cucumberContext.result.isSuccess shouldBe true
        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)
            cucumberContext.categoryOutputPort.countCategories(
                cucumberContext.storeId,
                beginsWith
            )
        }
    }
}
