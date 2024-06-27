package br.com.rodrigogurgel.catalogservice.application.steps.product

import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.input.product.CreateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CreateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.Before
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.verifySequence

class CreateProductStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val productContext: ProductContextStepDefs,
) {
    private val createProductUseCase: CreateProductUseCase =
        CreateProductInputPort(storeContext.storeDatastoreOutputPort, productContext.productDatastoreOutputPort)

    @Before
    fun setUp() {
        every { productContext.productDatastoreOutputPort.create(any(), any()) } returns Unit
    }

    @When("I add a product into store")
    fun iAddAProductIntoStore() {
        createProductUseCase.execute(storeContext.store.id, productContext.product)
    }

    @Then("the product should be persist in the datastore")
    fun theProductShouldBePersistInTheStore() {
        verifySequence {
            storeContext.storeDatastoreOutputPort.exists(storeContext.store.id)
            productContext.productDatastoreOutputPort.create(storeContext.store.id, productContext.product)
        }
    }

    @When("I try add a product into store with this id {string}")
    fun iTryAddAProductIntoStoreWithThisId(id: String) {
        shouldThrow<StoreNotFoundException> {
            createProductUseCase.execute(Id(id), productContext.product)
        }
    }
}
