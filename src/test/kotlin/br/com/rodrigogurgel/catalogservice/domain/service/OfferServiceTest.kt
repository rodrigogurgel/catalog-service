package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOffer
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOption
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test

class OfferServiceTest {
    @Test
    fun `Should return a list with only one element when call getAllProducts and offer don't have customizations`() {
        val offer = mockOffer()
        val ids = OfferService.getAllProducts(offer)

        ids shouldHaveSize 1
        ids shouldContain offer.product
    }

    @Test
    fun `Should return a list with only 12 products when call getAllProducts`() {
        val subOption1 = mockOptionWith {
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

        val subOption2 = mockOptionWith {
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

        val offer = mockOfferWith {
            customizations = mutableListOf(
                mockCustomizationWith {
                    options = mutableListOf(
                        mockOption(),
                        mockOption(),
                        subOption1
                    )
                },
                mockCustomizationWith {
                    options = mutableListOf(
                        mockOption()
                    )
                },
                mockCustomizationWith {
                    options = mutableListOf(
                        subOption2
                    )
                }
            )
        }
        val products = OfferService.getAllProducts(offer)

        products shouldHaveSize 12
    }
}
