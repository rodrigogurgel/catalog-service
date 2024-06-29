package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.CucumberContext
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.AddCustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.AddCustomizationOnChildrenInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.CreateOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.DeleteOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.GetOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.UpdateOfferInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.service.OfferService
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategoryWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.mockk.every
import java.util.UUID
import io.cucumber.java.en.Then
import io.kotest.matchers.shouldBe
import io.mockk.justRun
import io.mockk.verify
import io.mockk.verifyOrder
import io.mockk.verifySequence

class OfferStepDefs(
    private val cucumberContext: CucumberContext,
) {
    private lateinit var offerToBeCreated: Offer
    private lateinit var offerToBeUpdated: Offer
    private lateinit var customizationToBeAdded: Customization
    private lateinit var categoryId: Id

    private val createOfferInputPort = CreateOfferInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.categoryDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val updateOfferInputPort = UpdateOfferInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.productDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val getOfferInputPort = GetOfferInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val deleteOfferInputPort = DeleteOfferInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val addCustomizationInputPort = AddCustomizationInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.offerDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    private val addCustomizationOnChildrenInputPort = AddCustomizationOnChildrenInputPort(
        cucumberContext.storeRestOutputPort,
        cucumberContext.offerDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    @Given("the information of the Offer to be created")
    fun theInformationOfTheOfferToBeCreated(offer: Offer) {
        offerToBeCreated = offer
    }

    @And("that there is a Offer with the Id {string} in the Store with the Id {string}")
    fun thatThereIsAOfferWithTheIdInTheStoreWithTheId(offerIdString: String, storeIdString: String) {
        val offerId = Id(UUID.fromString(offerIdString))
        val storeId = Id(UUID.fromString(storeIdString))

        every { cucumberContext.offerDatastoreOutputPort.exists(offerId) } returns true
        every { cucumberContext.offerDatastoreOutputPort.exists(storeId, offerId) } returns true
        every { cucumberContext.offerDatastoreOutputPort.findById(storeId, offerId) } returns mockOfferWith {
            id = offerId
        }

        justRun {
            cucumberContext.offerDatastoreOutputPort.delete(storeId, offerId)
        }
    }

    @And("that there isn't a Offer with the Id {string}")
    fun thatThereIsnTAOfferWithTheId(offerIdString: String) {
        val offerId = Id(UUID.fromString(offerIdString))

        every { cucumberContext.offerDatastoreOutputPort.exists(offerId) } returns false
        every { cucumberContext.offerDatastoreOutputPort.findById(any(), offerId) } returns null

        justRun {
            cucumberContext.offerDatastoreOutputPort.create(
                any(),
                any(),
                match { offer -> offer.id == offerId })
        }

        justRun {
            cucumberContext.offerDatastoreOutputPort.update(
                any(),
                match { offer -> offer.id == offerId })
        }
    }

    @And("the Id of the Category is {string}")
    fun theIdOfTheCategoryIs(categoryIdString: String) {
        categoryId = Id(UUID.fromString(categoryIdString))
    }

    @When("I attempt to create a Offer")
    fun iAttemptToCreateAOffer() {
        cucumberContext.result = runCatching {
            createOfferInputPort.execute(cucumberContext.storeId, categoryId, offerToBeCreated)
        }
    }

    @Then("the Offer should be stored in the database")
    fun theOfferShouldBeStoredInTheDatabase() {
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)
            cucumberContext.categoryDatastoreOutputPort.exists(cucumberContext.storeId, categoryId)
            cucumberContext.offerDatastoreOutputPort.exists(offerToBeCreated.id)

            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                cucumberContext.storeId,
                OfferService.getAllProducts(offerToBeCreated).map { product -> product.id }
            )

            cucumberContext.offerDatastoreOutputPort.create(cucumberContext.storeId, categoryId, offerToBeCreated)
        }
    }

    @When("I attempt to create a Offer using the Id {string}")
    fun iAttemptToCreateAOfferUsingTheId(offerIdString: String) {
        val offer = offerToBeCreated.run {
            Offer(
                id = Id(UUID.fromString(offerIdString)),
                product,
                price,
                status,
                customizations
            )
        }
        cucumberContext.result = runCatching {
            createOfferInputPort.execute(cucumberContext.storeId, categoryId, offer)
        }
    }

    @And("that there is an Offer with the Id {string} in the Store with the Id {string}")
    fun thatThereIsAnOfferWithTheIdInTheStoreWithTheId(offerIdString: String, storeIdString: String) {
        val offerId = Id(UUID.fromString(offerIdString))

        val storeId = Id(UUID.fromString(storeIdString))

        every { cucumberContext.offerDatastoreOutputPort.exists(offerId) } returns true
        every { cucumberContext.offerDatastoreOutputPort.exists(storeId, offerId) } returns true

        justRun { cucumberContext.offerDatastoreOutputPort.update(storeId, match { offer -> offer.id == offerId }) }
    }

    @And("that there isn't an Offer with the Id {string}")
    fun thatThereIsnTAnOfferWithTheId(offerIdString: String) {
        val offerId = Id(UUID.fromString(offerIdString))

        every { cucumberContext.offerDatastoreOutputPort.exists(offerId) } returns false
        every { cucumberContext.offerDatastoreOutputPort.exists(any(), offerId) } returns false

        justRun {
            cucumberContext.offerDatastoreOutputPort.create(
                any(),
                any(),
                match { offer -> offer.id == offerId })
        }
    }

    @When("I attempt to create an Offer")
    fun iAttemptToCreateAnOffer() {
        cucumberContext.result = runCatching {
            createOfferInputPort.execute(cucumberContext.storeId, categoryId, offerToBeCreated)
        }
    }

    @When("I attempt to create an Offer using the Id {string}")
    fun iAttemptToCreateAnOfferUsingTheId(offerIdString: String) {
        val offer = offerToBeCreated.run {
            Offer(
                id = Id(UUID.fromString(offerIdString)),
                product,
                price,
                status,
                customizations
            )
        }
        cucumberContext.result = runCatching {
            createOfferInputPort.execute(cucumberContext.storeId, categoryId, offer)
        }
    }

    @Given("the information of the Offer to be updated")
    fun theInformationOfTheOfferToBeUpdated(offer: Offer) {
        offerToBeUpdated = offer

        every { cucumberContext.offerDatastoreOutputPort.findById(any(), offer.id) } returns offer
    }

    @When("I attempt to update an Offer")
    fun iAttemptToUpdateAnOffer() {
        cucumberContext.result = runCatching {
            updateOfferInputPort.execute(cucumberContext.storeId, offerToBeUpdated)
        }
    }

    @When("I attempt to update an Offer using the Id {string}")
    fun iAttemptToUpdateAnOfferUsingTheId(offerIdString: String) {
        val offer = offerToBeUpdated.run {
            Offer(
                id = Id(UUID.fromString(offerIdString)),
                product,
                price,
                status,
                customizations
            )
        }

        cucumberContext.result = runCatching {
            updateOfferInputPort.execute(cucumberContext.storeId, offer)
        }
    }

    @Then("the Offer should be updated in the database")
    fun theOfferShouldBeUpdatedInTheDatabase() {
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)
            cucumberContext.offerDatastoreOutputPort.exists(cucumberContext.storeId, offerToBeUpdated.id)

            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                cucumberContext.storeId,
                OfferService.getAllProducts(offerToBeUpdated).map { product -> product.id }
            )

            cucumberContext.offerDatastoreOutputPort.update(cucumberContext.storeId, offerToBeUpdated)
        }
    }

    @When("I attempt to get a Offer with the Id {string}")
    fun iAttemptToGetAOfferWithTheId(offerIdString: String) {
        cucumberContext.result = runCatching {
            getOfferInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(offerIdString)))
        }
    }

    @Then("the Offer with the Id {string} should be retrieved from database")
    fun theOfferWithTheIdShouldBeRetrievedFromDatabase(offerIdString: String) {
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(
                cucumberContext.storeId,
                Id(UUID.fromString(offerIdString))
            )
        }
    }

    @When("I attempt to delete a Offer with the Id {string}")
    fun iAttemptToDeleteAOfferWithTheId(offerIdString: String) {
        cucumberContext.result = runCatching {
            deleteOfferInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(offerIdString)))
        }
    }

    @Then("the Offer with the Id {string} should be deleted from database")
    fun theOfferWithTheIdShouldBeDeletedFromDatabase(offerIdString: String) {
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.delete(
                cucumberContext.storeId,
                Id(UUID.fromString(offerIdString))
            )
        }
    }

    @Given("the information of the Customization to be added")
    fun theInformationOfTheCustomizationToBeAdded(customization: Customization) {
        customizationToBeAdded = customization
    }

    @When("I attempt to add a Customization")
    fun iAttemptToAddACustomization() {
        cucumberContext.result = runCatching {
            addCustomizationInputPort.execute(cucumberContext.storeId, offerToBeUpdated.id, customizationToBeAdded)
        }
    }

    @And("the Customization should be added in the offer")
    fun theCustomizationShouldBeAddedInTheOffer() {
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offerToBeUpdated.id)

            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                cucumberContext.storeId,
                OfferService.getAllProducts(offerToBeUpdated).map { product -> product.id }
            )

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer -> offer.findCustomizationInChildrenById(customizationToBeAdded.id) != null }
            )
        }
    }

    @And("the Offer to be updated has a Customization with the Id {string}")
    fun theOfferToBeUpdatedHasACustomizationWithTheId(customizationIdString: String) {
        val customization = mockCustomizationWith {
            id = Id(UUID.fromString(customizationIdString))
        }

        offerToBeUpdated.addCustomization(customization)
    }

    @And("the Offer to be updated has a Option with the Id {string} in the Customization with the Id {string}")
    fun theOfferToBeUpdatedHasAOptionWithTheIdInTheCustomizationWithTheId(
        optionIdString: String,
        customizationIdString: String,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        val option = mockOptionWith {
            id = Id(UUID.fromString(optionIdString))
        }

        offerToBeUpdated.findCustomizationInChildrenById(customizationId)?.addOption(option)
    }

    @When("I attempt to add a Customization on children with the Id {string}")
    fun iAttemptToAddACustomizationOnChildrenWithTheId(optionIdString: String) {
        cucumberContext.result = runCatching {
            addCustomizationOnChildrenInputPort.execute(
                cucumberContext.storeId,
                offerToBeUpdated.id,
                Id(UUID.fromString(optionIdString)),
                customizationToBeAdded
            )
        }.onFailure { it.printStackTrace() }
    }

    @When("I attempt to add a Customization in the Offer with the Id {string}")
    fun iAttemptToAddACustomizationInTheOfferWithTheId(offerIdString: String) {
        cucumberContext.result = runCatching {
            addCustomizationInputPort.execute(
                cucumberContext.storeId,
                Id(UUID.fromString(offerIdString)),
                customizationToBeAdded
            )
        }.onFailure { it.printStackTrace() }
    }
}