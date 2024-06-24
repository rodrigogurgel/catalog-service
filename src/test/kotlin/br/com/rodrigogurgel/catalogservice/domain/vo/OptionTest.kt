package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OptionTest {
    @Test
    fun `Minimal price should be equal to 0 when Option price is 0 and customizations is empty`() {
        val product = mockProduct()
        val price = Price.ZERO
        val quantity = Quantity(0, 1)
        val status = Status.AVAILABLE
        val customizations = mutableListOf<Customization>()
        val option = Option(
            product,
            price,
            quantity,
            status,
            customizations,
        )

        assertEquals(product, option.product)
        assertEquals(price, option.price)
        assertEquals(status, option.status)
        assertEquals(quantity, option.quantity)
        assertEquals(customizations, option.customizations)

        assertEquals(Price.ZERO.normalizedValue(), option.minimalPrice().normalizedValue())
        assertEquals(0, option.quantity.minPermitted)
        assertTrue(option.customizations.isEmpty())
    }

    @Test
    fun `Minimal price should be equal to 10 when Option price is 10 and customizations is empty and minPermitted is 1`() {
        val option = Option(
            mockProduct(),
            Price(BigDecimal.TEN),
            Quantity(1, 1),
            Status.AVAILABLE,
            mutableListOf()
        )

        assertEquals(Price(BigDecimal.TEN).normalizedValue(), option.minimalPrice().normalizedValue())
        assertEquals(1, option.quantity.minPermitted)
        assertTrue(option.customizations.isEmpty())
    }

    @Test
    fun `Minimal price should be equal to 10 when Option price is 10 and customizations is empty and minPermitted is 0`() {
        val option = Option(
            mockProduct(),
            Price(BigDecimal.TEN),
            Quantity(0, 1),
            Status.AVAILABLE,
            mutableListOf()
        )

        assertEquals(Price(BigDecimal.TEN).normalizedValue(), option.minimalPrice().normalizedValue())
        assertEquals(0, option.quantity.minPermitted)
        assertTrue(option.customizations.isEmpty())
    }

    @Test
    fun `Minimal price should be equal to 20 when Option price is 10 and customizations is empty and minPermitted is 2`() {
        val option = Option(
            mockProduct(),
            Price(BigDecimal.TEN),
            Quantity(2, 2),
            Status.AVAILABLE,
            mutableListOf()
        )

        assertEquals(Price(BigDecimal.valueOf(20)).normalizedValue(), option.minimalPrice().normalizedValue())
        assertEquals(2, option.quantity.minPermitted)
        assertTrue(option.customizations.isEmpty())
    }
}
