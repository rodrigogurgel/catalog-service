package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOption
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test

class OptionServiceTest {

    @Test
    fun `Should return list with 4 products when call getAllProducts`() {
        val products = OptionService.getAllProducts(
            mockOptionWith {
                customizations = mutableListOf(
                    mockCustomizationWith {
                        options = mutableListOf(
                            mockOption(),
                            mockOption(),
                            mockOption()
                        )
                    }
                )
            }
        )

        products shouldHaveSize 4
    }
}
