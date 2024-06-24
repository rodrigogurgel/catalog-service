package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.exception.ProductNotFoundException
import br.com.rodrigogurgel.catalogservice.application.exception.StoreNotFoundException
import br.com.rodrigogurgel.catalogservice.application.port.`in`.product.GetProductInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

class GetProductStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val productContext: ProductContextStepDefs,
) {
    private lateinit var productRetrieve: Product

    private val getProductUseCase =
        GetProductInputPort(storeContext.storeDatastoreOutputPort, productContext.productDatastoreOutputPort)

    @When("I get a product with id {string} from store with id {string}")
    fun iGetAProductWithIdFromStoreWithId(productId: String, storeId: String) {
        productRetrieve = getProductUseCase.execute(Id(storeId), Id(productId))
    }

    @Then("the product should have same information's")
    fun theProductShouldHaveSameInformationS() {
        productRetrieve shouldBe productContext.product
    }

    @When("I try get a product from store with this id {string}")
    fun iTryGetAProductFromStoreWithThisId(storeId: String) {
        shouldThrow<StoreNotFoundException> {
            getProductUseCase.execute(Id(storeId), productContext.product.id)
        }
    }

    @When("I try get a product with this id {string} from store")
    fun iTryGetAProductWithThisIdFromStore(productId: String) {
        shouldThrow<ProductNotFoundException> {
            getProductUseCase.execute(storeContext.store.id, Id(productId))
        }
    }
}
