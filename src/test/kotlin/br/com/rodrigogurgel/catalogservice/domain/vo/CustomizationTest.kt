package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOption
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CustomizationTest {
    @Test
    fun `Should instantiate with success when minPermitted is equal to 0 and options is not empty`() {
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(0, 1)
        val status = Status.AVAILABLE
        val options = mutableListOf(mockOption())

        val customization = Customization(
            name,
            description,
            quantity,
            status,
            options
        )

        customization.name shouldBe name
        customization.description shouldBe description
        customization.quantity shouldBe quantity
        customization.status shouldBe status
        customization.options shouldBe options
    }

    @Test
    fun `Should instantiate with success when minPermitted is equal to 1 and options size is equal to minPermitted`() {
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(1, 1)
        val status = Status.AVAILABLE
        val options = mutableListOf(mockOption())

        val customization = Customization(
            name,
            description,
            quantity,
            status,
            options
        )

        customization.name shouldBe name
        customization.quantity shouldBe quantity
        customization.status shouldBe status
        customization.options shouldBe options
        customization.quantity.minPermitted shouldBe options.size
    }

    @Test
    fun `Should instantiate with error when maxPermitted is greater than options size and options is not empty`() {
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(1, 2)
        val status = Status.AVAILABLE
        val options = mutableListOf(mockOption())

        shouldThrow<IllegalArgumentException> {
            Customization(
                name,
                description,
                quantity,
                status,
                options
            )
        }
    }

    @Test
    fun `Should instantiate with error when options is empty`() {
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(0, 2)
        val status = Status.AVAILABLE
        val options = mutableListOf<Option>()

        shouldThrow<IllegalArgumentException> {
            Customization(
                name,
                description,
                quantity,
                status,
                options
            )
        }
    }

    @Test
    fun `Minimal value should be 10 when minPermitted is 2 and options has 2 options with minimal price equals to 5`() {
        val option1 = mockOptionWith {
            quantity = Quantity(2, 3)
            price = Price(2.5.toBigDecimal())
        }
        val option2 = mockOptionWith {
            quantity = Quantity(1, 3)
            price = Price(5.toBigDecimal())
        }

        val customization = mockCustomizationWith {
            quantity = Quantity(minPermitted = 2, maxPermitted = 2)
            options = mutableListOf(option1, option2)
        }

        customization.minimalPrice().normalizedValue() shouldBe Price(10.toBigDecimal()).normalizedValue()
    }

    @Test
    fun `Minimal value should be 10 when Customization minPermitted is 2 and Customization has 2 options with minimal price equals to 5 but Option price is equals to 0`() {
        val subOption1 = mockOptionWith {
            quantity = Quantity(minPermitted = 1, maxPermitted = 2)
            price = Price(2.5.toBigDecimal())
        }
        val subOption2 = mockOptionWith {
            quantity = Quantity(minPermitted = 0, maxPermitted = 1)
            price = Price(2.5.toBigDecimal())
        }

        val option1 = mockOptionWith {
            quantity = Quantity(2, 3)
            price = Price(BigDecimal.ZERO)
            customizations = mutableListOf(
                mockCustomizationWith {
                    quantity = Quantity(2, 2)
                    options = mutableListOf(subOption1, subOption2)
                }
            )
        }

        val subOption3 = mockOptionWith {
            quantity = Quantity(minPermitted = 1, maxPermitted = 2)
            price = Price(5.toBigDecimal())
        }
        val subOption4 = mockOptionWith {
            quantity = Quantity(minPermitted = 0, maxPermitted = 1)
            price = Price(10.toBigDecimal())
        }

        val option2 = mockOptionWith {
            quantity = Quantity(0, 3)
            price = Price(0.toBigDecimal())
            customizations = mutableListOf(
                mockCustomizationWith {
                    quantity = Quantity(1, 2)
                    options = mutableListOf(subOption3, subOption4)
                }
            )
        }

        option2.minimalPrice()

        val customization = mockCustomizationWith {
            quantity = Quantity(minPermitted = 2, maxPermitted = 2)
            options = mutableListOf(option1, option2)
        }

        customization.minimalPrice().normalizedValue() shouldBe Price(10.toBigDecimal()).normalizedValue()
    }
}
