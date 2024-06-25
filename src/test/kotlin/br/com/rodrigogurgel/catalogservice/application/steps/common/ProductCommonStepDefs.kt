package br.com.rodrigogurgel.catalogservice.application.steps.common

import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import io.cucumber.java.en.Given
import io.mockk.every

class ProductCommonStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val productContext: ProductContextStepDefs,
) {
    @Given("the following product exists:")
    fun theFollowingProductExists(product: Product) {
        productContext.product = product

        every {
            productContext.productDatastoreOutputPort.findById(
                storeContext.store.id,
                product.id
            )
        } returns product

        every {
            productContext.productDatastoreOutputPort.exists(
                storeContext.store.id,
                match { product.id == it }
            )
        } returns true

        every {
            productContext.productDatastoreOutputPort.update(
                storeContext.store.id,
                match { product.id == it.id }
            )
        } returns Unit

        every {
            productContext.productDatastoreOutputPort.getIfNotExists(
                storeContext.store.id,
                match { it == listOf(product.id) }
            )
        } returns emptyList()
    }

    @Given("a id {string} with no product associated")
    fun aIdWithNoProductAssociated(productId: String) {
        every {
            productContext.productDatastoreOutputPort.findById(
                storeContext.store.id,
                Id(productId)
            )
        } returns null
    }

    @Given("the following product information:")
    fun theFollowingProductInformation(product: Product) {
        productContext.product = product
    }
}
