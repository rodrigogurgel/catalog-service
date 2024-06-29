package br.com.rodrigogurgel.catalogservice.application

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Image
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategoryWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import io.cucumber.java.DataTableType
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite
import java.util.UUID

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
class RunCucumberTest {
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
        return mockProductWith {
            id = Id(UUID.fromString(entry["id"]!!))
            name = Name(entry["name"]!!)
            description = Description(entry["description"]!!)
            image = Image(entry["image"]!!)
        }
    }

    @DataTableType
    fun offerEntry(entry: Map<String, String>): Offer {
        return mockOfferWith {
            id = Id(UUID.fromString(entry["id"]!!))
            product = mockProductWith {
                id = Id(UUID.fromString(entry["product_id"]!!))
            }
            price = Price(entry["price"]!!.toBigDecimal())
            status = Status.valueOf(entry["status"]!!)
        }
    }

    @DataTableType
    fun customizationEntry(entry: Map<String, String>): Customization {
        return mockCustomizationWith {
            id = Id(UUID.fromString(entry["id"]!!))
            name = Name(entry["name"]!!)
            description = Description(entry["description"]!!)
        }
    }
}