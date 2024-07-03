package br.com.rodrigogurgel.catalogservice.application

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Image
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategoryWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.mockk.every
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import java.util.UUID

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
class RunCucumberTest(
    private val cucumberContext: CucumberContext,
) {
    @Before
    fun setUp() {
        every { cucumberContext.offerOutputPort.getOffersByProductIdIncludingChildren(any()) } returns emptyList()
    }

    @DataTableType
    fun categoryEntry(entry: Map<String, String>): Category {
        return mockCategoryWith {
            id = Id(UUID.fromString(entry["id"]!!))
            name = Name(entry["name"]!!)
            description = Description(entry["description"]!!)
            status = Status.valueOf(entry["status"]!!)
        }
    }

    @DataTableType
    fun productEntry(entry: Map<String, String>): Product {
        val product = mockProductWith {
            id = Id(UUID.fromString(entry["id"]!!))
            name = Name(entry["name"]!!)
            description = Description(entry["description"]!!)
            image = Image(entry["image"]!!)
        }

        cucumberContext.storeProducts[product.id] = product
        return product
    }

    @DataTableType
    fun offerEntry(entry: Map<String, String>): Offer {
        val product = cucumberContext.storeProducts[Id(UUID.fromString(entry["product_id"]!!))] ?: mockProductWith {
            id = Id(UUID.fromString(entry["product_id"]!!))
        }

        val offer = mockOfferWith {
            id = Id(UUID.fromString(entry["id"]!!))
            this.product = product
            price = Price(entry["price"]!!.toBigDecimal())
            status = Status.valueOf(entry["status"]!!)
        }

        OfferService.getAllProducts(offer).forEach {
            every { cucumberContext.offerOutputPort.getOffersByProductIdIncludingChildren(it.id) } returns listOf(offer.id)
        }

        return offer
    }

    @DataTableType
    fun customizationEntry(entry: Map<String, String>): Customization {
        return mockCustomizationWith {
            id = Id(UUID.fromString(entry["id"]!!))
            name = Name(entry["name"]!!)
            description = Description(entry["description"]!!)
        }
    }

    @DataTableType
    fun optionEntry(entry: Map<String, String>): Option {
        return mockOptionWith {
            id = Id(UUID.fromString(entry["id"]!!))
            product = cucumberContext.storeProducts[Id(UUID.fromString(entry["product_id"]!!))] ?: mockProductWith {
                id = Id(UUID.fromString(entry["product_id"]!!))
            }
            price = Price(entry["price"]!!.toBigDecimal())
            status = Status.valueOf(entry["status"]!!)
        }
    }
}
