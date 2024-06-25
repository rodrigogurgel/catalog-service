package br.com.rodrigogurgel.catalogservice.application.steps.common

import br.com.rodrigogurgel.catalogservice.application.context.CategoryContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.OfferContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.ProductContextStepDefs
import br.com.rodrigogurgel.catalogservice.application.context.StoreContextStepDefs
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.mockk.every
import java.math.BigDecimal

class OfferCommonStepDefs(
    private val storeContext: StoreContextStepDefs,
    private val categoryContext: CategoryContextStepDefs,
    private val productContext: ProductContextStepDefs,
    private val offerContext: OfferContextStepDefs,
) {
    @DataTableType
    fun offerEntry(entry: Map<String, String>): Offer {
        return mockOfferWith {
            id = entry["id"]?.let { Id(it) } ?: Id()
            price = Price(BigDecimal(entry["price"]!!))
            status = Status.valueOf(entry["status"]!!)
            product = productContext.product
        }
    }

    @Given("the following offer information:")
    fun theFollowingOfferInformation(offer: Offer) {
        offerContext.offer = offer
    }

    @Given("the following offer exists:")
    fun theFollowingOfferExists(offer: Offer) {
        offerContext.offer = offer

        every {
            offerContext.offerDatastoreOutputPort.findById(
                storeContext.store.id,
                categoryContext.category.id,
                offer.id
            )
        } returns offer

        every {
            offerContext.offerDatastoreOutputPort.delete(
                storeContext.store.id,
                categoryContext.category.id,
                offer.id
            )
        } returns Unit

        every {
            offerContext.offerDatastoreOutputPort.exists(
                storeContext.store.id,
                categoryContext.category.id,
                offer.id
            )
        } returns true

        every {
            offerContext.offerDatastoreOutputPort.update(
                storeContext.store.id,
                categoryContext.category.id,
                match { it.id == offer.id }
            )
        } returns Unit
    }

    @Given("a id {string} with no offer associated")
    fun aIdWithNoOfferAssociated(offerId: String) {
        every {
            offerContext.offerDatastoreOutputPort.findById(
                any(),
                any(),
                match { it == Id(offerId) }
            )
        } returns null

        every {
            offerContext.offerDatastoreOutputPort.exists(
                any(),
                any(),
                match { it == Id(offerId) }
            )
        } returns false
    }
}
