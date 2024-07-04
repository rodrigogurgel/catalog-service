package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.domain.exception.DuplicatedCustomizationException
import br.com.rodrigogurgel.catalogservice.domain.exception.DuplicatedOptionException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test

class OfferServiceTest {
    @Test
    fun `Should run validateDuplications successfully`() {
        val optionId = Id()
        val customizationId = Id()
        val offerId = Id()
        val option = mockOptionWith { id = optionId }
        val customization = mockCustomizationWith {
            id = customizationId
            options = mutableListOf(option)
        }
        val offer = mockOfferWith {
            id = offerId
            customizations = mutableListOf(customization)
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
            customizations = mutableListOf(customizationSub1, customizationSub2)
        }

        val customization = mockCustomizationWith {
            id = customizationId
            options = mutableListOf(option)
        }
        val offer = mockOfferWith {
            id = offerId
            customizations = mutableListOf(customization)
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
            options = mutableListOf(option1)
        }

        val customization2 = mockCustomizationWith {
            options = mutableListOf(option2)
        }

        val offer = mockOfferWith {
            id = offerId
            customizations = mutableListOf(customization1, customization2)
        }

        shouldThrow<DuplicatedOptionException> {
            OfferService.validateDuplications(offer)
        }
    }
}
