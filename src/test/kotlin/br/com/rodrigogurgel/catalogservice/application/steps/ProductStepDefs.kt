package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.CucumberContext
import br.com.rodrigogurgel.catalogservice.application.port.input.product.CountProductsInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.CreateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.DeleteProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.GetProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.GetProductsInputPort
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
    private lateinit var product: Product

    private val createProductInputPort = CreateProductInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.productOutputPort
    )

    private val deleteProductInputPort = DeleteProductInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.productOutputPort,
        cucumberContext.offerOutputPort
    )

    private val getProductInputPort = GetProductInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.productOutputPort
    )

    private val updateProductInputPort = UpdateProductInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.productOutputPort
    )

    private val getProductsInputPort = GetProductsInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.productOutputPort
    )

    private val countProductsInputPort = CountProductsInputPort(
        cucumberContext.storeOutputPort,
        cucumberContext.productOutputPort
    )

    @Given("the information of the Product")
    fun theInformationOfTheProduct(product: Product) {
        this.product = product
    }

    @When("I attempt to create a Product")
    fun iAttemptToCreateAProduct() {
        cucumberContext.result = runCatching {
            createProductInputPort.execute(cucumberContext.storeId, product)
        }
    }

    @Then("the Product should be stored in the database")
    fun theProductShouldBeStoredInTheDatabase() {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)
            cucumberContext.productOutputPort.exists(product.id)
            cucumberContext.productOutputPort.create(cucumberContext.storeId, product)
        }
    }

    @When("I attempt to create a Product using the Id {string}")
    fun iAttemptToCreateAProductUsingTheId(productIdString: String) {
        cucumberContext.result = runCatching {
            createProductInputPort.execute(
                cucumberContext.storeId,
                product.copy(
                    id = Id(UUID.fromString(productIdString))
                )
            )
        }
    }

    @When("I attempt to delete a Product with the Id {string}")
    fun iAttemptToDeleteAProductWithTheId(productIdString: String) {
        cucumberContext.result = runCatching {
            deleteProductInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(productIdString)))
        }.onFailure { it.printStackTrace() }
    }

    @And("that there isn't a Product with the Id {string}")
    fun thatThereIsnTAProductWithTheId(productIdString: String) {
        val productId = Id(UUID.fromString(productIdString))

        every { cucumberContext.productOutputPort.exists(productId) } returns false
        every { cucumberContext.productOutputPort.exists(any(), productId) } returns false

        justRun { cucumberContext.productOutputPort.create(any(), match { it.id == productId }) }

        every { cucumberContext.productOutputPort.findById(any(), productId) } returns null

        every {
            cucumberContext.productOutputPort.getIfNotExists(
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

        every { cucumberContext.productOutputPort.exists(productId) } returns true
        every { cucumberContext.productOutputPort.exists(storeId, productId) } returns true

        justRun { cucumberContext.productOutputPort.delete(storeId, productId) }
        justRun {
            cucumberContext.productOutputPort.update(
                storeId,
                match { product -> product.id == productId }
            )
        }

        every { cucumberContext.productOutputPort.findById(storeId, productId) } returns product
        every {
            cucumberContext.productOutputPort.getIfNotExists(
                storeId,
                listOf(productId)
            )
        } returns emptyList()
    }

    @Then("the Product with the Id {string} should be deleted from database")
    fun theProductWithTheIdShouldBeDeletedFromDatabase(productIdString: String) {
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)
            cucumberContext.productOutputPort.delete(
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
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)
            cucumberContext.productOutputPort.findById(
                cucumberContext.storeId,
                Id(UUID.fromString(productIdString))
            )
        }
    }

    fun iAttemptToUpdateAProductUsingTheId(productIdString: String) {
        val productId = Id(UUID.fromString(productIdString))

        cucumberContext.result = runCatching {
            updateProductInputPort.execute(
                cucumberContext.storeId,
                product.copy(id = productId)
            )
        }
    }

    @Then("the Product should be updated in the database")
    fun theProductShouldBeUpdatedInTheDatabase() {
        verifySequence {
            cucumberContext.result.exceptionOrNull()?.printStackTrace()
            cucumberContext.result.isSuccess shouldBe true

            cucumberContext.productOutputPort.exists(
                cucumberContext.storeId,
                product.id
            )

            cucumberContext.productOutputPort.update(
                cucumberContext.storeId,
                product
            )
        }
    }

    @When("I attempt to update a Product")
    fun iAttemptToUpdateAProduct() {
        cucumberContext.result = runCatching {
            updateProductInputPort.execute(
                cucumberContext.storeId,
                product
            )
        }
    }

    @When("I attempt to update a Product with the Id {string}")
    fun iAttemptToUpdateAProductWithTheId(productIdString: String) {
        cucumberContext.result = runCatching {
            updateProductInputPort.execute(
                cucumberContext.storeId,
                product.copy(id = Id(UUID.fromString(productIdString))),
            )
        }
    }

    @And("that there is the following Products in the Store with the Id {string}")
    fun thatThereIsTheFollowingProductsInTheStoreWithTheId(storeIdString: String, products: List<Product>) {
        val storeId = Id(UUID.fromString(storeIdString))

        products.forEach { product ->
            every { cucumberContext.productOutputPort.exists(product.id) } returns true
            every { cucumberContext.productOutputPort.exists(storeId, product.id) } returns true

            justRun { cucumberContext.productOutputPort.delete(storeId, product.id) }
            justRun {
                cucumberContext.productOutputPort.update(
                    storeId,
                    product
                )
            }
            every { cucumberContext.productOutputPort.findById(storeId, product.id) } returns product
        }

        every {
            cucumberContext.productOutputPort.getIfNotExists(
                storeId,
                match { ids ->
                    products.map { product -> product.id }.containsAll(ids)
                }
            )
        } returns emptyList()

        cucumberContext.storeProducts.putAll(products.associateBy { it.id })
    }

    @When("I attempt to get a Products with the limit as {string}, offset as {string} and begins with as {string}")
    fun iAttemptToGetAProductsWithTheLimitAsOffsetAsAndBeginsWithAs(limit: String, offset: String, beginsWith: String) {
        cucumberContext.result = runCatching {
            getProductsInputPort.execute(cucumberContext.storeId, limit.toInt(), offset.toInt(), beginsWith)
        }.onFailure { it.printStackTrace() }
    }

    @Then(
        "the Products should be retrieved from database with the limit as {string}, offset as {string} and begins with as {string}"
    )
    fun theProductsShouldBeRetrievedFromDatabaseWithTheLimitAsOffsetAsAndBeginsWithAs(
        limit: String,
        offset: String,
        beginsWith: String,
    ) {
        cucumberContext.result.isSuccess shouldBe true
        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)
            cucumberContext.productOutputPort.getProducts(
                cucumberContext.storeId,
                limit.toInt(),
                offset.toInt(),
                beginsWith
            )
        }
    }

    @Then(
        "the Products should be counted in the database with the begins with as {string}"
    )
    fun theProductsShouldBeCountedInTheDatabaseWithTheLimitAsOffsetAsAndBeginsWithAs(
        beginsWith: String,
    ) {
        cucumberContext.result.isSuccess shouldBe true
        verifySequence {
            cucumberContext.storeOutputPort.exists(cucumberContext.storeId)
            cucumberContext.productOutputPort.countProducts(
                cucumberContext.storeId,
                beginsWith
            )
        }
    }

    @When("I attempt to count the Products with the begins with as {string}")
    fun iAttemptToCountTheProductsWithTheLimitAsOffsetAsAndBeginsWithAs(
        beginsWith: String,
    ) {
        cucumberContext.result = runCatching {
            countProductsInputPort.execute(cucumberContext.storeId, beginsWith)
        }.onFailure { it.printStackTrace() }
    }
}
