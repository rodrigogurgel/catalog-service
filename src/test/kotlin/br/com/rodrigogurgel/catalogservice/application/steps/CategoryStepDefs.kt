package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.CucumberContext
import br.com.rodrigogurgel.catalogservice.application.port.input.category.CreateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.DeleteCategoryInputPort
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
        cucumberContext.storeRestOutputPort,
        cucumberContext.categoryDatastoreOutputPort
    )

    private val deleteCategoryInputPort = DeleteCategoryInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.categoryDatastoreOutputPort
    )

    private val getCategoryInputPort = GetCategoryInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.categoryDatastoreOutputPort
    )

    private val updateCategoryInputPort = UpdateCategoryInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.categoryDatastoreOutputPort
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
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)
            cucumberContext.categoryDatastoreOutputPort.exists(
                category.id
            )
            cucumberContext.categoryDatastoreOutputPort.create(cucumberContext.storeId, category)
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
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)

            cucumberContext.categoryDatastoreOutputPort.delete(
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
            cucumberContext.categoryDatastoreOutputPort.exists(
                id
            )
        } returns false

        every {
            cucumberContext.categoryDatastoreOutputPort.exists(
                any(),
                id
            )
        } returns false

        justRun {
            cucumberContext.categoryDatastoreOutputPort.create(
                any(),
                match { category -> category.id == id }
            )
        }

        every {
            cucumberContext.categoryDatastoreOutputPort.findById(
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
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)

            cucumberContext.categoryDatastoreOutputPort.findById(
                cucumberContext.storeId,
                match { id -> id == Id(UUID.fromString(categoryIdString)) }
            )
        }
    }

    @Then("the Category should be updated in the database")
    fun theCategoryShouldBeUpdatedInTheDatabase() {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)

            cucumberContext.categoryDatastoreOutputPort.exists(
                cucumberContext.storeId,
                category.id
            )

            cucumberContext.categoryDatastoreOutputPort.update(
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
        every { cucumberContext.categoryDatastoreOutputPort.exists(categoryId) } returns true

        every {
            cucumberContext.categoryDatastoreOutputPort.exists(
                storeId,
                categoryId
            )
        } returns true

        every {
            cucumberContext.categoryDatastoreOutputPort.findById(
                storeId,
                categoryId
            )
        } returns category

        justRun {
            cucumberContext.categoryDatastoreOutputPort.delete(
                storeId,
                categoryId
            )
        }

        justRun {
            cucumberContext.categoryDatastoreOutputPort.update(
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
}
