package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OfferTest {
    @Test
    fun `Should instantiate Offer with success when price is greater than 0`() {
        val id = Id()
        val product = mockProduct()
        val customizations = mutableListOf(
            mockCustomizationWith {
                quantity = Quantity(1, 1)
                options = mutableListOf(
                    mockOptionWith {
                        quantity = Quantity(1, 1)
                        price = Price(10.toBigDecimal())
                    }
                )
            }
        )
        val price = Price.ZERO
        val status = Status.AVAILABLE

        val offer = Offer(
            id,
            product,
            price,
            status,
            customizations
        )

        assertEquals(id, offer.id)
        assertEquals(product, offer.product)
        assertEquals(price, offer.price)
        assertEquals(status, offer.status)
        assertEquals(customizations, offer.customizations)
        assertEquals(Price(10.toBigDecimal()).normalizedValue(), offer.minimalPrice().normalizedValue())
    }

    @Test
    fun `Should instantiate Offer with error when price is equals to 0`() {
        val id = Id()
        val product = mockProduct()

        val status = Status.AVAILABLE

        shouldThrow<IllegalStateException> {
            Offer(id, product, Price.ZERO, status, mutableListOf())
        }
        val customizations = mutableListOf(
            mockCustomizationWith {
                quantity = Quantity(0, 1)
                options = mutableListOf(
                    mockOptionWith {
                        quantity = Quantity(0, 1)
                        price = Price(10.toBigDecimal())
                    }
                )
            }
        )

        shouldThrow<IllegalStateException> {
            Offer(id, product, Price.ZERO, status, customizations)
        }
    }

    /**
     * Case:
     *
     *      Offer
     *      price 7.50
     *      minimalPrice 8.50
     *
     *          Customization 1
     *          quantity minPermitted 2
     *          quantity maxPermitted 3
     *          minimalPrice 1
     *
     *              Option 1
     *              quantity minPermitted 0
     *              quantity maxPermitted 10
     *              price 10.50
     *              minimalPrice 10.50
     *
     *              Option 2
     *              quantity minPermitted 1
     *              quantity maxPermitted 5
     *              price 0.50
     *              minimalPrice 0.50
     *
     *              Option 3
     *              quantity minPermitted 1
     *              quantity maxPermitted 1
     *              price 1.50
     *              minimalPrice 1.50
     *
     *              Option 4
     *              quantity minPermitted 2
     *              quantity maxPermitted 5
     *              price 0.25
     *              minimalPrice 0.50
     */
    @Test
    fun `Should be equals to 8,5 when call minimalPrice`() {
        val option1 = mockOptionWith {
            quantity = Quantity(0, 10)
            price = Price(10.50.toBigDecimal())
        }
        val option2 = mockOptionWith {
            quantity = Quantity(1, 5)
            price = Price(0.50.toBigDecimal())
        }
        val option3 = mockOptionWith {
            quantity = Quantity(1, 1)
            price = Price(1.50.toBigDecimal())
        }
        val option4 = mockOptionWith {
            quantity = Quantity(2, 10)
            price = Price(0.25.toBigDecimal())
        }

        val customization1 = mockCustomizationWith {
            quantity = Quantity(2, 3)
            options = mutableListOf(option1, option2, option3, option4)
        }

        val item = mockOfferWith {
            price = Price(7.50.toBigDecimal())
            customizations = mutableListOf(customization1)
        }

        assertEquals(Price(8.5.toBigDecimal()).normalizedValue(), item.minimalPrice().normalizedValue())
    }

    /**
     *  Case:
     *
     *      Offer
     *      price 7.50
     *      minimalPrice 9.50
     *
     *          Customization 1
     *          quantity minPermitted 2
     *          quantity maxPermitted 3
     *          minimalPrice 1
     *
     *              Option 1
     *              quantity minPermitted 0
     *              quantity maxPermitted 10
     *              price 10.50
     *              minimalPrice 38.0
     *
     *                  Customization 3
     *                  quantity minPermitted 2
     *                  quantity maxPermitted 3
     *                  minimalPrice 27.50
     *
     *                      Option 7
     *                      quantity minPermitted 0
     *                      quantity maxPermitted 10
     *                      price 10.50
     *                      minimalPrice 10.50
     *
     *                      Option 8
     *                      quantity minPermitted 2
     *                      quantity maxPermitted 5
     *                      price 8.50
     *                      minimalPrice 17
     *
     *              Option 2
     *              quantity minPermitted 1
     *              quantity maxPermitted 5
     *              price 0.50
     *              minimalPrice 0.50
     *
     *              Option 3
     *              quantity minPermitted 1
     *              quantity maxPermitted 1
     *              price 1.50
     *              minimalPrice 1.50
     *
     *              Option 4
     *              quantity minPermitted 2
     *              quantity maxPermitted 5
     *              price 0.25
     *              minimalPrice 0.50
     *
     *          Customization 2
     *          quantity minPermitted 2
     *          quantity maxPermitted 3
     *          minimalPrice 1
     *
     *              Option 5
     *              quantity minPermitted 0
     *              quantity maxPermitted 10
     *              price 0.50
     *              minimalPrice 0.50
     *
     *              Option 6
     *              quantity minPermitted 1
     *              quantity maxPermitted 5
     *              price 0.50
     *              minimalPrice 0.50
     */

    @Test
    fun `Should be equals to 9,5 when call minimalPrice`() {
        val option7 = mockOptionWith {
            quantity = Quantity(0, 10)
            price = Price(10.50.toBigDecimal())
        }

        val option8 = mockOptionWith {
            quantity = Quantity(1, 10)
            price = Price(.50.toBigDecimal())
        }

        val option1 = mockOptionWith {
            quantity = Quantity(0, 2)
            price = Price(10.50.toBigDecimal())
            customizations = mutableListOf(
                mockCustomizationWith {
                    quantity = Quantity(0, 2)
                    options = mutableListOf(option7, option8)
                }
            )
        }

        val option2 = mockOptionWith {
            quantity = Quantity(1, 5)
            price = Price(0.50.toBigDecimal())
        }
        val option3 = mockOptionWith {
            quantity = Quantity(1, 1)
            price = Price(1.50.toBigDecimal())
        }
        val option4 = mockOptionWith {
            quantity = Quantity(2, 10)
            price = Price(0.25.toBigDecimal())
        }

        val customization1 = mockCustomizationWith {
            quantity = Quantity(2, 3)
            options = mutableListOf(option1, option2, option3, option4)
        }

        val option5 = mockOptionWith {
            quantity = Quantity(0, 10)
            price = Price(.50.toBigDecimal())
        }

        val option6 = mockOptionWith {
            quantity = Quantity(1, 5)
            price = Price(.50.toBigDecimal())
        }

        val customization2 = mockCustomizationWith {
            quantity = Quantity(2, 2)
            options = mutableListOf(option5, option6)
        }

        val item = mockOfferWith {
            price = Price(7.50.toBigDecimal())
            customizations = mutableListOf(customization1, customization2)
        }

        assertEquals(Price(9.5.toBigDecimal()).normalizedValue(), item.minimalPrice().normalizedValue())
    }
}
