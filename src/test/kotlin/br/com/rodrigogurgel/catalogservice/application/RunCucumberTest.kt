package br.com.rodrigogurgel.catalogservice.application

import br.com.rodrigogurgel.catalogservice.domain.entity.Category
import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.entity.Store
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Image
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategoryWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProductWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockStoreWith
import io.cucumber.java.DataTableType
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
class RunCucumberTest {
    @DataTableType
    fun storeEntry(entry: Map<String, String>): Store {
        val storeId = Id(entry["id"]!!)
        val storeName = Name(entry["name"]!!)
        val storeDescription = Description(entry["description"]!!)

        return mockStoreWith {
            id = storeId
            name = storeName
            description = storeDescription
        }
    }

    @DataTableType
    fun categoryEntry(entry: Map<String, String>): Category {
        return mockCategoryWith {
            id = entry["id"]?.let { Id(it) } ?: Id()
            name = Name(entry["name"]!!)
            description = Description(entry["description"]!!)
            status = Status.valueOf(entry["status"]!!)
        }
    }

    @DataTableType
    fun productEntry(entry: Map<String, String>): Product {
        return mockProductWith {
            id = entry["id"]?.let { Id(it) } ?: Id()
            name = Name(entry["name"]!!)
            description = Description(entry["description"]!!)
            image = Image(entry["image"]!!)
        }
    }
}
