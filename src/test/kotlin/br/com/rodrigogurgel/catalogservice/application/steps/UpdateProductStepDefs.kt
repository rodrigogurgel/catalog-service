package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.ProductNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.product.UpdateProductInputPort
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.every
import io.mockk.verifySequence
import org.junit.jupiter.api.assertThrows

class UpdateProductStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val productContext: ProductContextStepDefs,
) {
    private val updateProductUseCase =
        UpdateProductInputPort(storeContext.storeDatastoreOutputPort, productContext.productDatastoreOutputPort)

    @Before
    fun setUp() {
        every {
            productContext.productDatastoreOutputPort.exists(
                any(),
                match { it == Id("92ddeebf-da50-402f-b850-19e5fb093a0a") }
            )
        } throws ProductNotFoundException(Id("92ddeebf-da50-402f-b850-19e5fb093a0a"))
    }

    @When("I update product name to {string} into store")
    fun iUpdateProductNameToIntoStore(newProductName: String) {
        updateProductUseCase.execute(
            storeContext.store.id,
            productContext.product.copy(
                name = Name(newProductName)
            )
        )
    }

    @When("I try update a product with this id {string} from store")
    fun iTryUpdateAProductWithThisIdFromStore(productId: String) {
        assertThrows<ProductNotFoundException> {
            updateProductUseCase.execute(
                storeContext.store.id,
                mockProductWith {
                    id = Id(productId)
                }
            )
        }
    }

    @Then("the product with new information's should be persist in the datastore")
    fun theProductWithNewInformationSShouldBePersistInTheDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            productContext.productDatastoreOutputPort.exists(storeContext.store.id, any())
            productContext.productDatastoreOutputPort.update(
                storeContext.store.id,
                match { productContext.product.id == it.id }
            )
        }
    }
}
