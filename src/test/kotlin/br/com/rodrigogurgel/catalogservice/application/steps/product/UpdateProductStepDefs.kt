package br.com.rodrigogurgel.catalogservice.application.steps.product

import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.ProductNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.product.UpdateProductInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.verifySequence

class UpdateProductStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val productContext: ProductContextStepDefs,
) {
    private lateinit var updatedProduct: Product

    private val updateProductUseCase =
        UpdateProductInputPort(storeContext.storeDatastoreOutputPort, productContext.productDatastoreOutputPort)

    @Before
    fun setUp() {
        every {
            productContext.productDatastoreOutputPort.exists(
                any(),
                match { it == Id("92ddeebf-da50-402f-b850-19e5fb093a0a") }
            )
        } returns false
    }

    @When("I update product name to {string} into store")
    fun iUpdateProductNameToIntoStore(newProductName: String) {
        updatedProduct = productContext.product.copy(
            name = Name(newProductName)
        )
        updateProductUseCase.execute(
            storeContext.store.id,
            updatedProduct
        )
    }

    @When("I try update a product with this id {string} from store")
    fun iTryUpdateAProductWithThisIdFromStore(productId: String) {
        val exception = shouldThrow<ProductNotFoundException> {
            updateProductUseCase.execute(
                storeContext.store.id,
                mockProductWith {
                    id = Id(productId)
                }
            )
        }

        exception.message shouldContain productId
    }

    @Then("the product with new information should be persist in the datastore")
    fun theProductWithNewInformationShouldBePersistInTheDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            productContext.productDatastoreOutputPort.exists(storeContext.store.id, any())
            productContext.productDatastoreOutputPort.update(
                storeContext.store.id,
                updatedProduct
            )
        }
    }

    @When("I try update a product into store with this id {string}")
    fun iTryUpdateAProductIntoStoreWithThisId(storeId: String) {
        val exception = shouldThrow<StoreNotFoundException> {
            updateProductUseCase.execute(
                Id(storeId),
                productContext.product
            )
        }

        exception.message shouldContain storeId
    }
}
