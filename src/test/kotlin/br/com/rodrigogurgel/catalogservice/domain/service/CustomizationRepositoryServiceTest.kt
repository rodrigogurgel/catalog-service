package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomization
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOption
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test

class CustomizationRepositoryServiceTest {
    @Test
    fun `Should return a list with 1 product when call getAllProducts`() {
        val customization = mockCustomization()
        val ids = CustomizationService.getAllProducts(customization)

        ids shouldHaveSize 1
    }

    @Test
    fun `Should return list with 4 products when call getAllProducts`() {
        val option = mockOptionWith {
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

        val products = CustomizationService.getAllProducts(
            mockCustomizationWith {
                options = mutableListOf(option)
            }
        )

        products shouldHaveSize 4
    }
}
