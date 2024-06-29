package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomization
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOption
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OptionTest {
    @Test
    fun `Minimal price should be equal to 0 when Option price is 0 and customizations is empty`() {
        val id = Id()
        val product = mockProduct()
        val price = Price.ZERO
        val quantity = Quantity(0, 1)
        val status = Status.AVAILABLE
        val customizations = mutableListOf<Customization>()
        val option = Option(
            id,
            product,
            price,
            quantity,
            status,
            customizations,
        )

        option.id shouldBe id
        option.product shouldBe product
        option.price shouldBe price
        option.status shouldBe status
        option.quantity shouldBe quantity
        option.customizations shouldBe customizations

        option.minimalPrice().normalizedValue() shouldBe Price.ZERO.normalizedValue()
        option.quantity.minPermitted shouldBe 0
        option.customizations.isEmpty() shouldBe true
    }

    @Test
    fun `Should update mutable values with success`() {
        val id = Id()
        val product = mockProduct()
        val price = Price.ZERO
        val quantity = Quantity(0, 1)
        val status = Status.AVAILABLE
        val customizations = mutableListOf<Customization>()
        val option = Option(
            id,
            product,
            price,
            quantity,
            status,
            customizations,
        )

        option.id shouldBe id
        option.product shouldBe product
        option.price shouldBe price
        option.status shouldBe status
        option.quantity shouldBe quantity
        option.customizations shouldBe customizations
        option.quantity.minPermitted shouldBe 0
        option.customizations.isEmpty() shouldBe true

        val updatedProduct = mockProduct()
        val updatedPrice = Price(10.toBigDecimal())
        val updatedQuantity = Quantity(2, 2)

        option.product = updatedProduct
        option.price = updatedPrice
        option.status = Status.UNAVAILABLE
        option.quantity = updatedQuantity

        option.product shouldBe updatedProduct
        option.price shouldBe updatedPrice
        option.status shouldBe Status.UNAVAILABLE
        option.quantity shouldBe updatedQuantity
    }

    @Test
    fun `Minimal price should be equal to 10 when Option price is 10 and customizations is empty and minPermitted is 1`() {
        val option = Option(
            Id(),
            mockProduct(),
            Price(BigDecimal.TEN),
            Quantity(1, 1),
            Status.AVAILABLE,
            mutableListOf()
        )

        option.minimalPrice().normalizedValue() shouldBe Price(BigDecimal.TEN).normalizedValue()
        option.quantity.minPermitted shouldBe 1
        option.customizations.isEmpty() shouldBe true
    }

    @Test
    fun `Minimal price should be equal to 10 when Option price is 10 and customizations is empty and minPermitted is 0`() {
        val option = Option(
            Id(),
            mockProduct(),
            Price(BigDecimal.TEN),
            Quantity(0, 1),
            Status.AVAILABLE,
            mutableListOf()
        )

        option.minimalPrice().normalizedValue() shouldBe Price(BigDecimal.TEN).normalizedValue()
        option.quantity.minPermitted shouldBe 0
        option.customizations.isEmpty() shouldBe true
    }

    @Test
    fun `Minimal price should be equal to 20 when Option price is 10 and customizations is empty and minPermitted is 2`() {
        val option = Option(
            Id(),
            mockProduct(),
            Price(BigDecimal.TEN),
            Quantity(2, 2),
            Status.AVAILABLE,
            mutableListOf()
        )

        option.minimalPrice().normalizedValue() shouldBe Price(BigDecimal.valueOf(20)).normalizedValue()
        option.quantity.minPermitted shouldBe 2
        option.customizations.isEmpty() shouldBe true
    }

    @Test
    fun `Should add Customization with success when call addCustomization`() {
        val customization = mockCustomization()
        val option = mockOption()

        option.addCustomization(customization)

        option.customizations shouldContain customization
    }

    @Test
    fun `Should add Customization with error when call addCustomization and customization already exists`() {
        val customization = mockCustomization()
        val option = mockOptionWith {
            customizations = listOf(customization)
        }

        shouldThrow<CustomizationAlreadyExistsException> {
            option.addCustomization(customization)
        }

        option.customizations shouldContain customization
    }

    @Test
    fun `Should update Customization with success when call updateCustomization`() {
        val customization = mockCustomization()
        val updatedCustomization = mockCustomizationWith { id = customization.id }
        val option = mockOptionWith { customizations = listOf(customization) }

        option.customizations shouldContain customization

        option.updateCustomization(updatedCustomization)

        option.customizations shouldContain updatedCustomization
    }

    @Test
    fun `Should update Customization with error when call updateCustomization and customization not exists`() {
        val customization = mockCustomization()
        val newCustomization = mockCustomization()
        val option = mockOptionWith { customizations = listOf(customization) }

        option.customizations shouldContain customization

        shouldThrow<CustomizationNotFoundException> {
            option.updateCustomization(newCustomization)
        }

        option.customizations shouldNotContain newCustomization
    }

    @Test
    fun `Should remove Customization with success when call updateCustomization`() {
        val customization = mockCustomization()
        val option = mockOptionWith {
            customizations = listOf(customization)
        }

        option.customizations shouldContain customization

        option.removeCustomization(customization.id)

        option.customizations shouldNotContain customization
    }
}
