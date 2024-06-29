package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.domain.exception.DuplicatedCustomizationException
import br.com.rodrigogurgel.catalogservice.domain.exception.DuplicatedOptionException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOffer
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOption
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import io.kotest.assertions.throwables.shouldThrow
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

    @Test
    fun `Should run validateDuplications successfully`() {
        val optionId = Id()
        val customizationId = Id()
        val offerId = Id()
        val option = mockOptionWith { id = optionId }
        val customization = mockCustomizationWith {
            id = customizationId
            options = listOf(option)
        }
        val offer = mockOfferWith {
            id = offerId
            customizations = listOf(customization)
        }

        OfferService.validateDuplications(offer)
    }

    @Test
    fun `Should throw exception DuplicatedCustomizationException when call validateDuplications`() {
        val optionId = Id()
        val customizationId = Id()
        val offerId = Id()
        val customizationSub1 = mockCustomizationWith {
            id = customizationId
        }
        val customizationSub2 = mockCustomizationWith {
            id = customizationId
        }
        val option = mockOptionWith {
            id = optionId
            customizations = listOf(customizationSub1, customizationSub2)
        }

        val customization = mockCustomizationWith {
            id = customizationId
            options = listOf(option)
        }
        val offer = mockOfferWith {
            id = offerId
            customizations = listOf(customization)
        }

        shouldThrow<DuplicatedCustomizationException> {
            OfferService.validateDuplications(offer)
        }
    }

    @Test
    fun `Should throw exception DuplicatedOptionException when call validateDuplications`() {
        val optionId = Id()
        val offerId = Id()

        val option1 = mockOptionWith {
            id = optionId
        }

        val option2 = mockOptionWith {
            id = optionId
        }

        val customization1 = mockCustomizationWith {
            options = listOf(option1)
        }

        val customization2 = mockCustomizationWith {
            options = listOf(option2)
        }

        val offer = mockOfferWith {
            id = offerId
            customizations = listOf(customization1, customization2)
        }

        shouldThrow<DuplicatedOptionException> {
            OfferService.validateDuplications(offer)
        }
    }
}
