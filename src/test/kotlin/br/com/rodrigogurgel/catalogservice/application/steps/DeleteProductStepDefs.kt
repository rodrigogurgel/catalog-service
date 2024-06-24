package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.product.DeleteProductInputPort
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.verifySequence

class DeleteProductStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val productContext: ProductContextStepDefs,
) {
    private val deleteProductUseCase =
        DeleteProductInputPort(storeContext.storeDatastoreOutputPort, productContext.productDatastoreOutputPort)

    @Before
    fun setUp() {
        every {
            productContext.productDatastoreOutputPort.delete(any(), any())
        } returns Unit
    }

    @When("I delete a product from store")
    fun iDeleteAProductFromStore() {
        deleteProductUseCase.execute(storeContext.store.id, productContext.product.id)
    }

    @Then("the product should be remove from datastore")
    fun theProductShouldBeRemoveFromDatastore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            productContext.productDatastoreOutputPort.delete(storeContext.store.id, productContext.product.id)
        }
    }

    @When("I try remove a product from store with this id {string}")
    fun iTryRemoveAProductFromStoreWithThisId(id: String) {
        shouldThrow<StoreNotFoundException> {
            deleteProductUseCase.execute(Id(id), productContext.product.id)
        }
    }
}
