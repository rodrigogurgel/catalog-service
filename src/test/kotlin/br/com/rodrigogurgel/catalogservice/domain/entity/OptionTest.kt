package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Customization
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
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
}
