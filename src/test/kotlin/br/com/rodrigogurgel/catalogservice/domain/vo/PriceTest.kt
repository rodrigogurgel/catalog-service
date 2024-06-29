package br.com.rodrigogurgel.catalogservice.domain.vo

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.math.RoundingMode
import kotlin.random.Random

class PriceTest {
    @Test
    fun `Should instantiate price with success when value is greater than 0`() {
        val moreThan0 = Random.nextDouble(0.1, 1000000.0).toBigDecimal().setScale(2, RoundingMode.UP)
        val price = Price(moreThan0)
        price.normalizedValue() shouldBe moreThan0
    }

    @Test
    fun `Should instantiate price with error when value is less than 0`() {
        val negative = -1.0
        shouldThrow<IllegalArgumentException> {
            Price(negative.toBigDecimal())
        }
    }
}
