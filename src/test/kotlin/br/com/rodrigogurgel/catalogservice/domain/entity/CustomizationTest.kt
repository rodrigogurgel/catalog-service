package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationMaxPermittedException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationOptionsIsEmptyException
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.OptionNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Description
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomization
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOption
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class CustomizationTest {
    @Test
    fun `Should instantiate with success when minPermitted is equal to 0 and options is not empty`() {
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(0, 1)
        val status = Status.AVAILABLE
        val options = mutableListOf(mockOption())

        val customization = Customization(
            id,
            name,
            description,
            quantity,
            status,
            options
        )

        customization.id shouldBe id
        customization.name shouldBe name
        customization.description shouldBe description
        customization.quantity shouldBe quantity
        customization.status shouldBe status
        customization.options shouldBe options
    }

    @Test
    fun `Should instantiate with error when options size with status AVAILABLE is less than minPermitted`() {
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(1, 1)
        val status = Status.AVAILABLE
        val option = mockOption()

        val customization = Customization(
            id,
            name,
            description,
            quantity,
            status,
            listOf(option)
        )

        customization.id shouldBe id
        customization.name shouldBe name
        customization.description shouldBe description
        customization.quantity shouldBe quantity
        customization.status shouldBe status
        customization.options shouldContain option

        option.status = Status.UNAVAILABLE

        shouldThrow<CustomizationMaxPermittedException> {
            customization.updateOption(option)
        }
    }

    @Test
    fun `Should update mutable values with success`() {
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(0, 1)
        val status = Status.AVAILABLE
        val options = mutableListOf(mockOption())

        val customization = Customization(
            id,
            name,
            description,
            quantity,
            status,
            options
        )

        customization.id shouldBe id
        customization.name shouldBe name
        customization.description shouldBe description
        customization.quantity shouldBe quantity
        customization.status shouldBe status
        customization.options shouldBe options

        val updatedName = Name(randomString(30))
        val updatedDescription = Description(randomString(1000))
        val updatedQuantity = Quantity(1, 1)

        customization.status = Status.UNAVAILABLE
        customization.name = updatedName
        customization.description = updatedDescription
        customization.setQuantity(updatedQuantity)

        customization.id shouldBe id
        customization.name.value shouldBe updatedName.value
        customization.description?.value shouldBe updatedDescription.value
        customization.quantity shouldBe updatedQuantity
        customization.status shouldBe Status.UNAVAILABLE
        customization.options shouldBe options
    }

    @Test
    fun `Should instantiate with success when minPermitted is equal to 1 and options size is equal to minPermitted`() {
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(1, 1)
        val status = Status.AVAILABLE
        val options = mutableListOf(mockOption())

        val customization = Customization(
            id,
            name,
            description,
            quantity,
            status,
            options
        )

        customization.id shouldBe id
        customization.name shouldBe name
        customization.quantity shouldBe quantity
        customization.status shouldBe status
        customization.options shouldBe options
        customization.quantity.minPermitted shouldBe options.size
    }

    @Test
    fun `Should instantiate with error when maxPermitted is greater than options size and options is not empty`() {
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(1, 2)
        val status = Status.AVAILABLE
        val options = mutableListOf(mockOption())

        shouldThrow<CustomizationMaxPermittedException> {
            Customization(
                id,
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
        val id = Id()
        val name = Name(randomString(30))
        val description = Description(randomString(1000))
        val quantity = Quantity(0, 2)
        val status = Status.AVAILABLE
        val options = mutableListOf<Option>()

        val exception = shouldThrow<CustomizationOptionsIsEmptyException> {
            Customization(
                id,
                name,
                description,
                quantity,
                status,
                options
            )
        }

        exception.message shouldContain id.toString()
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

    @Test
    fun `Should add option with success`() {
        val option = mockOption()
        val customization = mockCustomization()

        customization.addOption(option)

        customization.options shouldContain option
    }

    @Test
    fun `Should add option with error when option already exists in customization`() {
        val option = mockOption()
        val customization = mockCustomization()

        customization.addOption(option)
        shouldThrow<OptionAlreadyExistsException> {
            customization.addOption(option)
        }

        customization.options shouldContain option
    }

    @Test
    fun `Should remove option with success`() {
        val option = mockOption()
        val customization = mockCustomization()

        customization.addOption(option)

        customization.options shouldContain option

        customization.removeOption(option.id)

        customization.options shouldNotContain option
    }

    @Test
    fun `Should update option with error when option not exists in customization`() {
        val option = mockOption()
        val customization = mockCustomization()

        customization.options shouldNotContain option

        shouldThrow<OptionNotFoundException> {
            customization.removeOption(option.id)
        }
    }

    @Test
    fun `Should update option with success`() {
        val option = mockOption()
        val updatedOption = mockOptionWith {
            id = option.id
        }
        val customization = mockCustomization()

        customization.addOption(option)

        customization.options shouldContain option

        customization.updateOption(updatedOption)

        customization.options shouldContain updatedOption
    }

    @Test
    fun `Should remove option with error when option not exists in customization`() {
        val option = mockOption()
        val newOption = mockOption()
        val customization = mockCustomization()

        customization.addOption(option)

        customization.options shouldContain option

        assertThrows<OptionNotFoundException> {
            customization.updateOption(newOption)
        }

        customization.options shouldNotContain newOption
    }
}
