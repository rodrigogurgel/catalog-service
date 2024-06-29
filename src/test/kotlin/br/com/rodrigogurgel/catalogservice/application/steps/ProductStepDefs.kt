package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.CucumberContext
import br.com.rodrigogurgel.catalogservice.application.port.input.product.CreateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.DeleteProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.GetProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.UpdateProductInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.verifySequence
import java.util.UUID

class ProductStepDefs(
    private val cucumberContext: CucumberContext,
) {
    private lateinit var productToBeCreated: Product
    private lateinit var productToBeUpdated: Product

    private val createProductInputPort = CreateProductInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    private val deleteProductInputPort = DeleteProductInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    private val getProductInputPort = GetProductInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    private val updateProductInputPort = UpdateProductInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    @Given("the information of the Product to be created")
    fun theInformationOfTheProductToBeCreated(product: Product) {
        productToBeCreated = product
    }

    @When("I attempt to create a Product")
    fun iAttemptToCreateAProduct() {
        cucumberContext.result = runCatching {
            createProductInputPort.execute(cucumberContext.storeId, productToBeCreated)
        }
    }

    @Then("the Product should be stored in the database")
    fun theProductShouldBeStoredInTheDatabase() {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)
            cucumberContext.productDatastoreOutputPort.exists(productToBeCreated.id)
            cucumberContext.productDatastoreOutputPort.create(cucumberContext.storeId, productToBeCreated)
        }
    }

    @When("I attempt to create a Product using the Id {string}")
    fun iAttemptToCreateAProductUsingTheId(productIdString: String) {
        cucumberContext.result = runCatching {
            createProductInputPort.execute(
                cucumberContext.storeId,
                productToBeCreated.copy(
                    id = Id(UUID.fromString(productIdString))
                )
            )
        }
    }

    @When("I attempt to delete a Product with the Id {string}")
    fun iAttemptToDeleteAProductWithTheId(productIdString: String) {
        cucumberContext.result = runCatching {
            deleteProductInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(productIdString)))
        }
    }

    @And("that there isn't a Product with the Id {string}")
    fun thatThereIsnTAProductWithTheId(productIdString: String) {
        val productId = Id(UUID.fromString(productIdString))

        every { cucumberContext.productDatastoreOutputPort.exists(productId) } returns false
        every { cucumberContext.productDatastoreOutputPort.exists(any(), productId) } returns false

        justRun { cucumberContext.productDatastoreOutputPort.create(any(), match { it.id == productId }) }

        every { cucumberContext.productDatastoreOutputPort.findById(any(), productId) } returns null

        every {
            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                any(),
                match { ids -> productId in ids }
            )
        } returns listOf(productId)
    }

    @And("that there is a Product with the Id {string} in the Store with the Id {string}")
    fun thatThereIsAProductWithTheIdInTheStoreWithTheId(productIdString: String, storeIdString: String) {
        val productId = Id(UUID.fromString(productIdString))
        val storeId = Id(UUID.fromString(storeIdString))
        val product = mockProductWith { id = productId }

        every { cucumberContext.productDatastoreOutputPort.exists(productId) } returns true
        every { cucumberContext.productDatastoreOutputPort.exists(storeId, productId) } returns true

        justRun { cucumberContext.productDatastoreOutputPort.delete(storeId, productId) }
        justRun {
            cucumberContext.productDatastoreOutputPort.update(
                storeId,
                match { product -> product.id == productId }
            )
        }

        every { cucumberContext.productDatastoreOutputPort.findById(storeId, productId) } returns product
        every {
            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                storeId,
                match { ids -> productId in ids }
            )
        } returns emptyList()
    }

    @Then("the Product with the Id {string} should be deleted from database")
    fun theProductWithTheIdShouldBeDeletedFromDatabase(productIdString: String) {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)
            cucumberContext.productDatastoreOutputPort.delete(
                cucumberContext.storeId,
                Id(UUID.fromString(productIdString))
            )
        }
    }

    @When("I attempt to get a Product with the Id {string}")
    fun iAttemptToGetAProductWithTheId(productIdString: String) {
        cucumberContext.result = runCatching {
            getProductInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(productIdString)))
        }
    }

    @Then("the Product with the Id {string} should be retrieved from database")
    fun theProductWithTheIdShouldBeRetrievedFromDatabase(productIdString: String) {
        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)
            cucumberContext.productDatastoreOutputPort.findById(
                cucumberContext.storeId,
                Id(UUID.fromString(productIdString))
            )
        }
    }

    @Given("the information of the Product to be updated")
    fun theInformationOfTheProductToBeUpdated(product: Product) {
        productToBeUpdated = product
    }

    @When("I attempt to update a Product using the Id {string}")
    fun iAttemptToUpdateAProductUsingTheId(productIdString: String) {
        val productId = Id(UUID.fromString(productIdString))

        cucumberContext.result = runCatching {
            updateProductInputPort.execute(
                cucumberContext.storeId,
                productToBeUpdated.copy(id = productId)
            )
        }
    }

    @Then("the Product should be updated in the database")
    fun theProductShouldBeUpdatedInTheDatabase() {
        verifySequence {
            cucumberContext.result.exceptionOrNull()?.printStackTrace()
            cucumberContext.result.isSuccess shouldBe true

            cucumberContext.productDatastoreOutputPort.exists(
                cucumberContext.storeId,
                productToBeUpdated.id
            )

            cucumberContext.productDatastoreOutputPort.update(
                cucumberContext.storeId,
                productToBeUpdated
            )
        }
    }

    @When("I attempt to update a Product")
    fun iAttemptToUpdateAProduct() {
        cucumberContext.result = runCatching {
            updateProductInputPort.execute(
                cucumberContext.storeId,
                productToBeUpdated
            )
        }
    }

    @When("I attempt to update a Product with the Id {string}")
    fun iAttemptToUpdateAProductWithTheId(productIdString: String) {
        cucumberContext.result = runCatching {
            updateProductInputPort.execute(
                cucumberContext.storeId,
                productToBeUpdated.copy(id = Id(UUID.fromString(productIdString))),
            )
        }
    }
}
