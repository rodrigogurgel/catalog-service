package br.com.rodrigogurgel.catalogservice.domain.entity

import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationAlreadyExistsException
import br.com.rodrigogurgel.catalogservice.domain.exception.CustomizationNotFoundException
import br.com.rodrigogurgel.catalogservice.domain.exception.OfferPriceZeroException
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Name
import br.com.rodrigogurgel.catalogservice.domain.vo.Price
import br.com.rodrigogurgel.catalogservice.domain.vo.Quantity
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomization
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOffer
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOption
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OfferTest {
    @Test
    fun `Should instantiate Offer with success when price is greater than 0`() {
        val id = Id()
        val name = Name(randomString(30))
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
        val price = Price(BigDecimal.TEN)
        val status = Status.AVAILABLE

        val offer = Offer(
            id,
            name,
            product,
            price,
            status,
            customizations
        )

        offer.id shouldBe id
        offer.name shouldBe name
        offer.product shouldBe product
        offer.price shouldBe price
        offer.status shouldBe status
        offer.customizations shouldBe customizations
        offer.minimalPrice().value shouldBe Price(20.toBigDecimal()).value
    }

    @Test
    fun `Should update mutable values with success`() {
        val name = Name(randomString(30))
        val product = mockProduct()
        val price = Price(20.toBigDecimal())
        val status = Status.AVAILABLE

        val offer = Offer(
            Id(),
            name,
            product,
            price,
            status,
            mutableListOf()
        )

        offer.product shouldBe product
        offer.price shouldBe price
        offer.status shouldBe status

        val updatedProduct = mockProduct()
        val updatedPrice = Price(10.toBigDecimal())
        val updatedName = Name(randomString(30))

        offer.name = updatedName
        offer.product = updatedProduct
        offer.price = updatedPrice
        offer.status = Status.UNAVAILABLE

        offer.name shouldBe updatedName
        offer.product shouldBe updatedProduct
        offer.price shouldBe updatedPrice
        offer.status shouldBe Status.UNAVAILABLE
    }

    @Test
    fun `Should instantiate Offer with error when price is equals to 0`() {
        val id = Id()
        val name = Name(randomString(30))
        val product = mockProduct()

        val status = Status.AVAILABLE

        shouldThrow<OfferPriceZeroException> {
            Offer(id, name, product, Price.ZERO, status, mutableListOf())
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

        shouldThrow<OfferPriceZeroException> {
            Offer(id, name, product, Price.ZERO, status, customizations)
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

        val offer = mockOfferWith {
            price = Price(7.50.toBigDecimal())
            customizations = mutableListOf(customization1)
        }

        offer.minimalPrice().value shouldBe Price(8.5.toBigDecimal()).value
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

        val offer = mockOfferWith {
            price = Price(7.50.toBigDecimal())
            customizations = mutableListOf(customization1, customization2)
        }

        offer.minimalPrice().value shouldBe Price(9.5.toBigDecimal()).value
    }

    @Test
    fun `Should add Customization with success when call addCustomization`() {
        val customization = mockCustomization()
        val offer = mockOffer()

        offer.addCustomization(customization)

        offer.customizations shouldContain customization
    }

    @Test
    fun `Should add Customization with error when call addCustomization and customization already exists`() {
        val customization = mockCustomization()
        val offer = mockOfferWith {
            customizations = mutableListOf(customization)
        }

        shouldThrow<CustomizationAlreadyExistsException> {
            offer.addCustomization(customization)
        }

        offer.customizations shouldContain customization
    }

    @Test
    fun `Should update Customization with success when call updateCustomization`() {
        val customization = mockCustomization()
        val updatedCustomization = mockCustomizationWith { id = customization.id }
        val offer = mockOfferWith { customizations = mutableListOf(customization) }

        offer.customizations shouldContain customization

        offer.updateCustomization(updatedCustomization)

        offer.customizations shouldContain updatedCustomization
    }

    @Test
    fun `Should update Customization with error when call updateCustomization and customization not exists`() {
        val customization = mockCustomization()
        val newCustomization = mockCustomization()
        val offer = mockOfferWith { customizations = mutableListOf(customization) }

        offer.customizations shouldContain customization

        shouldThrow<CustomizationNotFoundException> {
            offer.updateCustomization(newCustomization)
        }

        offer.customizations shouldNotContain newCustomization
    }

    @Test
    fun `Should remove Customization with success when call updateCustomization`() {
        val customization = mockCustomization()
        val offer = mockOfferWith {
            customizations = mutableListOf(customization)
        }

        offer.customizations shouldContain customization

        offer.removeCustomization(customization.id)

        offer.customizations shouldNotContain customization
    }

    @Test
    fun `Should return a Customization when call findCustomizationInChildrenById and Offer has the Customization`() {
        val customization = mockCustomization()
        val offer = mockOfferWith {
            customizations = mutableListOf(customization)
        }

        val result = offer.findCustomizationInChildrenById(customization.id)
        result.shouldNotBeNull()
        result shouldBe customization
    }

    @Test
    fun `Should return a Customization when call findCustomizationInChildrenById and some child has the Customization`() {
        val childCustomization = mockCustomization()
        val childOption = mockOptionWith {
            customizations = mutableListOf(childCustomization)
        }
        val customization = mockCustomizationWith {
            options = mutableListOf(childOption)
        }
        val offer = mockOfferWith {
            customizations = mutableListOf(customization)
        }

        val result = offer.findCustomizationInChildrenById(childCustomization.id)
        result.shouldNotBeNull()
        result shouldBe childCustomization
    }

    @Test
    fun `Should return an Option when call findOptionInChildrenById and some child has the Option`() {
        val childOption = mockOption()
        val customization = mockCustomizationWith {
            options = mutableListOf(childOption)
        }
        val offer = mockOfferWith {
            customizations = mutableListOf(customization)
        }

        val result = offer.findOptionInChildrenById(childOption.id)
        result.shouldNotBeNull()
        result shouldBe childOption
    }

    @Test
    fun `Should return a list with only one element when call getAllProducts and offer don't have customizations`() {
        val offer = mockOffer()
        val ids = offer.getAllProducts()

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
        val products = offer.getAllProducts()

        products shouldHaveSize 12
    }

    @Test
    fun `Should be equals`() {
        val offer = mockOffer()
        val other = offer.run {
            Offer(id, name, product, price, status, customizations)
        }

        offer shouldBeEqual offer
        other shouldBeEqual offer
    }

    @Test
    fun `Should have same hash code`() {
        val offer = mockOffer()
        val other = offer.run {
            Offer(id, name, product, price, status, customizations)
        }

        other shouldHaveSameHashCodeAs offer
    }
}
