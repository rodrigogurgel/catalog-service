package br.com.rodrigogurgel.catalogservice.application.steps

import br.com.rodrigogurgel.catalogservice.application.CucumberContext
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.AddCustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.AddCustomizationOnChildrenInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.AddOptionOnChildrenInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.CreateOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.DeleteOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.GetOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.RemoveCustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.RemoveCustomizationOnChildrenInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.RemoveOptionOnChildrenInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.UpdateCustomizationInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.UpdateCustomizationOnChildrenInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.UpdateOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.UpdateOptionOnChildrenInputPort
import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomizationWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOptionWith
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.verifySequence
import java.util.UUID

class OfferStepDefs(
    private val cucumberContext: CucumberContext,
) {
    private lateinit var categoryId: Id
    private lateinit var offer: Offer
    private val offers: MutableMap<Id, Offer> = mutableMapOf()
    private val customizations: MutableMap<Id, Customization> = mutableMapOf()
    private val options: MutableMap<Id, Option> = mutableMapOf()

    private val createOfferInputPort = CreateOfferInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.categoryDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val updateOfferInputPort = UpdateOfferInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val getOfferInputPort = GetOfferInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val deleteOfferInputPort = DeleteOfferInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val addCustomizationInputPort = AddCustomizationInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    private val addCustomizationOnChildrenInputPort = AddCustomizationOnChildrenInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    private val addOptionOnChildrenInputPort = AddOptionOnChildrenInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort
    )

    private val removeCustomizationInputPort = RemoveCustomizationInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort,
    )

    private val removeCustomizationOnChildrenInputPort = RemoveCustomizationOnChildrenInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val removeOptionOnChildrenInputPort = RemoveOptionOnChildrenInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort
    )

    private val updateCustomizationInputPort = UpdateCustomizationInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort,
    )

    private val updateCustomizationOnChildrenInputPort = UpdateCustomizationOnChildrenInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort,
    )

    private val updateOptionOnChildrenInputPort = UpdateOptionOnChildrenInputPort(
        cucumberContext.storeDatastoreOutputPort,
        cucumberContext.offerDatastoreOutputPort,
        cucumberContext.productDatastoreOutputPort,
    )

    @Given("the information of the Offer")
    fun theInformationOfTheOffer(offer: Offer) {
        this.offer = offer
        offers[offer.id] = offer
        every { cucumberContext.productDatastoreOutputPort.productIsInUse(offer.product.id) } returns true
    }

    @And("the Id of the Category is {string}")
    fun theIdOfTheCategoryIs(categoryIdString: String) {
        categoryId = Id(UUID.fromString(categoryIdString))
    }

    @Then("the Offer should be stored in the database")
    fun theOfferShouldBeStoredInTheDatabase() {
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)
            cucumberContext.categoryDatastoreOutputPort.exists(cucumberContext.storeId, categoryId)
            cucumberContext.offerDatastoreOutputPort.exists(offer.id)

            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                offer.getAllProducts().map { product -> product.id }
            )

            cucumberContext.offerDatastoreOutputPort.create(cucumberContext.storeId, categoryId, offer)
        }
    }

    @And("that there is an Offer with the Id {string} in the Store with the Id {string}")
    fun thatThereIsAnOfferWithTheIdInTheStoreWithTheId(offerIdString: String, storeIdString: String) {
        val offerId = Id(UUID.fromString(offerIdString))
        val storeId = Id(UUID.fromString(storeIdString))

        every { cucumberContext.offerDatastoreOutputPort.exists(offerId) } returns true
        every { cucumberContext.offerDatastoreOutputPort.exists(storeId, offerId) } returns true
        every { cucumberContext.offerDatastoreOutputPort.findById(storeId, offerId) } returns (
            offers[offerId]
                ?: mockOfferWith {
                    id = offerId
                }
            )

        justRun { cucumberContext.offerDatastoreOutputPort.update(storeId, match { offer -> offer.id == offerId }) }
        justRun {
            cucumberContext.offerDatastoreOutputPort.delete(storeId, offerId)
        }
    }

    @And("that there isn't an Offer with the Id {string}")
    fun thatThereIsnTAnOfferWithTheId(offerIdString: String) {
        val offerId = Id(UUID.fromString(offerIdString))

        every { cucumberContext.offerDatastoreOutputPort.exists(offerId) } returns false
        every { cucumberContext.offerDatastoreOutputPort.exists(any(), offerId) } returns false
        every { cucumberContext.offerDatastoreOutputPort.findById(any(), offerId) } returns null

        justRun {
            cucumberContext.offerDatastoreOutputPort.create(
                any(),
                any(),
                match { offer -> offer.id == offerId }
            )
        }

        justRun {
            cucumberContext.offerDatastoreOutputPort.update(
                any(),
                match { offer -> offer.id == offerId }
            )
        }
    }

    @When("I attempt to create an Offer")
    fun iAttemptToCreateAnOffer() {
        cucumberContext.result = runCatching {
            createOfferInputPort.execute(cucumberContext.storeId, categoryId, offer)
        }
    }

    @When("I attempt to create an Offer using the Id {string}")
    fun iAttemptToCreateAnOfferUsingTheId(offerIdString: String) {
        val offer = offer.run {
            Offer(
                Id(UUID.fromString(offerIdString)),
                name,
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

    @When("I attempt to update an Offer")
    fun iAttemptToUpdateAnOffer() {
        cucumberContext.result = runCatching {
            updateOfferInputPort.execute(cucumberContext.storeId, offer)
        }
    }

    @When("I attempt to update an Offer using the Id {string}")
    fun iAttemptToUpdateAnOfferUsingTheId(offerIdString: String) {
        val offer = offer.run {
            Offer(
                id = Id(UUID.fromString(offerIdString)),
                name,
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
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)
            cucumberContext.offerDatastoreOutputPort.exists(cucumberContext.storeId, offer.id)

            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                offer.getAllProducts().map { product -> product.id }
            )

            cucumberContext.offerDatastoreOutputPort.update(cucumberContext.storeId, offer)
        }
    }

    @When("I attempt to get an Offer with the Id {string}")
    fun iAttemptToGetAnOfferWithTheId(offerIdString: String) {
        cucumberContext.result = runCatching {
            getOfferInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(offerIdString)))
        }
    }

    @Then("the Offer with the Id {string} should be retrieved from database")
    fun theOfferWithTheIdShouldBeRetrievedFromDatabase(offerIdString: String) {
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(
                cucumberContext.storeId,
                Id(UUID.fromString(offerIdString))
            )
        }
    }

    @When("I attempt to delete an Offer with the Id {string}")
    fun iAttemptToDeleteAnOfferWithTheId(offerIdString: String) {
        cucumberContext.result = runCatching {
            deleteOfferInputPort.execute(cucumberContext.storeId, Id(UUID.fromString(offerIdString)))
        }
    }

    @Then("the Offer with the Id {string} should be deleted from database")
    fun theOfferWithTheIdShouldBeDeletedFromDatabase(offerIdString: String) {
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.delete(
                cucumberContext.storeId,
                Id(UUID.fromString(offerIdString))
            )
        }
    }

//    @And("the Customization should be added in the offer")
//    fun theCustomizationShouldBeAddedInTheOffer() {
//        cucumberContext.result.exceptionOrNull()?.printStackTrace()
//        cucumberContext.result.isSuccess shouldBe true
//
//        verifySequence {
//            cucumberContext.storeRestOutputPort.exists(cucumberContext.storeId)
//
//            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)
//
//            cucumberContext.productDatastoreOutputPort.getIfNotExists(
//                cucumberContext.storeId,
//                offer.getAllProducts().map { product -> product.id }
//            )
//
//            cucumberContext.offerDatastoreOutputPort.update(
//                cucumberContext.storeId,
//                match { offer -> offer.findCustomizationInChildrenById(customization.id) != null }
//            )
//        }
//    }

    @And("the Offer to be updated has a Customization with the Id {string}")
    fun theOfferToBeUpdatedHasACustomizationWithTheId(customizationIdString: String) {
        val customization = mockCustomizationWith {
            id = Id(UUID.fromString(customizationIdString))
        }

        offer.addCustomization(customization)
    }

    @And("the Offer to be updated has an Option with the Id {string} in the Customization with the Id {string}")
    fun theOfferToBeUpdatedHasAnOptionWithTheIdInTheCustomizationWithTheId(
        optionIdString: String,
        customizationIdString: String,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        val option = mockOptionWith {
            id = Id(UUID.fromString(optionIdString))
        }

        offer.findCustomizationInChildrenById(customizationId)?.addOption(option)
    }

    @And("the Customization with the Id {string} has the following Options")
    fun theCustomizationWithTheIdHasTheFollowingOptions(
        customizationIdString: String,
        options: List<Option>,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        customizations[customizationId]?.run {
            Customization(
                id,
                name,
                description,
                quantity,
                status,
                options.toMutableList()
            )
        }?.let { customization ->
            customizations[customizationId] = customization
            this.options.putAll(options.associateBy { it.id })
        }
    }

    @Then("the Customization with the Id {string} should be added in the offer")
    fun theCustomizationWithTheIdShouldBeAddedInTheOffer(customizationIdString: String) {
        val customizationId = Id(UUID.fromString(customizationIdString))

        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)

            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                match { ids -> ids.toSet() == offer.getAllProducts().map { product -> product.id }.toSet() }
            )

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer -> offer.findCustomizationInChildrenById(customizationId) != null }
            )
        }
    }

    @And("the following Customizations")
    fun theFollowingCustomizations(customizations: List<Customization>) {
        this.customizations.putAll(customizations.associateBy { customization -> customization.id })
    }

    @When("I attempt to add a Customization with the Id {string} in the Offer with the Id {string}")
    fun iAttemptToAddACustomizationWithTheIdInTheOfferWithTheId(customizationIdString: String, offerIdString: String) {
        val offerId = Id(UUID.fromString(offerIdString))
        val customizationId = Id(UUID.fromString(customizationIdString))

        cucumberContext.result = runCatching {
            addCustomizationInputPort.execute(
                cucumberContext.storeId,
                offerId,
                customizations[customizationId]!!
            )
        }.onFailure { it.printStackTrace() }
    }

    @And("that the Offer with the Id {string} has the Customization with the Id {string}")
    fun thatTheOfferWithTheIdHasTheCustomizationWithTheId(offerIdString: String, customizationIdString: String) {
        val offerId = Id(UUID.fromString(offerIdString))
        val customizationId = Id(UUID.fromString(customizationIdString))

        offers[offerId]?.let { offer ->
            customizations[customizationId]?.let { customization ->
                offer.addCustomization(customization)
            }
        }
    }

    @When(
        "I attempt to add a Customization with the Id {string} on children with the Id {string} in the Offer with the Id {string}"
    )
    fun iAttemptToAddACustomizationWithTheIdOnChildrenWithTheIdInTheOfferWithTheId(
        customizationIdString: String,
        optionIdString: String,
        offerIdString: String,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        val offerId = Id(UUID.fromString(offerIdString))
        val optionId = Id(UUID.fromString(optionIdString))
        cucumberContext.result = runCatching {
            addCustomizationOnChildrenInputPort.execute(
                cucumberContext.storeId,
                offerId,
                optionId,
                customizations[customizationId]!!
            )
        }
    }

    @When(
        "I attempt to add an Option with the Id {string} on children with the Id {string} in the Offer with the Id {string}"
    )
    fun iAttemptToAddAnOptionWithTheIdOnChildrenWithTheIdInTheOfferWithTheId(
        optionIdString: String,
        customizationIdString: String,
        offerIdString: String,
    ) {
        val optionId = Id(UUID.fromString(optionIdString))
        val customizationId = Id(UUID.fromString(customizationIdString))
        val offerId = Id(UUID.fromString(offerIdString))

        cucumberContext.result = runCatching {
            addOptionOnChildrenInputPort.execute(
                cucumberContext.storeId,
                offerId,
                customizationId,
                options[optionId]!!
            )
        }.onFailure { it.printStackTrace() }
    }

    @And("the following Options")
    fun theFollowingOptions(options: List<Option>) {
        this.options.putAll(options.associateBy { option -> option.id })
    }

    @Then("the Option with the Id {string} should be added in the Offer")
    fun theOptionWithTheIdShouldBeAddedInTheOffer(optionIdString: String) {
        val optionId = Id(UUID.fromString(optionIdString))

        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)

            cucumberContext.productDatastoreOutputPort.getIfNotExists(
                offer.getAllProducts().map { product -> product.id }
            )

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer -> offer.findOptionInChildrenById(optionId) != null }
            )
        }
    }

    @When("I attempt to remove a Customization with the Id {string} from the Offer with the Id {string}")
    fun iAttemptToRemoveACustomizationWithTheIdFromTheOfferWithTheId(
        customizationIdString: String,
        offerIdString: String,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        val offerId = Id(UUID.fromString(offerIdString))

        cucumberContext.result = runCatching {
            removeCustomizationInputPort.execute(cucumberContext.storeId, offerId, customizationId)
        }
    }

    @Then("the Customization with the Id {string} should be removed from the Offer")
    fun theCustomizationWithTheIdShouldBeRemovedFromTheOffer(customizationIdString: String) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer ->
                    offer.customizations.none { it.id == customizationId }
                }
            )
        }
    }

    @When(
        "I attempt to remove a Customization with the Id {string} from child with the Id {string} in the Offer with the Id {string}"
    )
    fun iAttemptToRemoveACustomizationWithTheIdOnChildWithTheIdInTheOfferWithTheId(
        customizationIdString: String,
        optionIdString: String,
        offerIdString: String,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        val offerId = Id(UUID.fromString(offerIdString))
        val optionId = Id(UUID.fromString(optionIdString))

        cucumberContext.result = runCatching {
            removeCustomizationOnChildrenInputPort.execute(cucumberContext.storeId, offerId, optionId, customizationId)
        }.onFailure { it.printStackTrace() }
    }

    @Then("the Customization with the Id {string} should be removed from Offer's children")
    fun theCustomizationWithTheIdShouldBeRemovedFromOfferSChildren(customizationIdString: String) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer ->
                    offer.findCustomizationInChildrenById(customizationId) == null
                }
            )
        }
    }

    @And("the Option with the Id {string} has the Customization with the Id {string}")
    fun theOptionWithTheIdHasTheCustomizationWithTheId(optionIdString: String, customizationIdString: String) {
        val optionId = Id(UUID.fromString(optionIdString))
        val customizationId = Id(UUID.fromString(customizationIdString))
        options[optionId]?.let { option ->
            customizations[customizationId]?.let { customization -> option.addCustomization(customization) }
        }
    }

    @When(
        "I attempt to remove an Option with the Id {string} from child with the Id {string} in the Offer with the Id {string}"
    )
    fun iAttemptToRemoveAnOptionWithTheIdFromChildWithTheIdInTheOfferWithTheId(
        optionIdString: String,
        customizationIdString: String,
        offerIdString: String,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        val optionId = Id(UUID.fromString(optionIdString))
        val offerId = Id(UUID.fromString(offerIdString))

        cucumberContext.result = runCatching {
            removeOptionOnChildrenInputPort.execute(cucumberContext.storeId, offerId, customizationId, optionId)
        }.onFailure { it.printStackTrace() }
    }

    @Then("the Option with the Id {string} should be removed from Offer's children")
    fun theOptionWithTheIdShouldBeRemovedFromOfferSChildren(optionIdString: String) {
        val optionId = Id(UUID.fromString(optionIdString))
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer ->
                    offer.findOptionInChildrenById(optionId) == null
                }
            )
        }
    }

    @When("I attempt to update a Customization with the Id {string} from the Offer with the Id {string}")
    fun iAttemptToUpdateACustomizationWithTheIdFromTheOfferWithTheId(
        customizationIdString: String,
        offerIdString: String,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        val offerId = Id(UUID.fromString(offerIdString))

        cucumberContext.result = runCatching {
            updateCustomizationInputPort.execute(cucumberContext.storeId, offerId, customizations[customizationId]!!)
        }.onFailure { it.printStackTrace() }
    }

    @Then("the Customization with the Id {string} should be updated from the Offer")
    fun theCustomizationWithTheIdShouldBeUpdatedFromTheOffer(customizationIdString: String) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer ->
                    offer.customizations
                        .any { customization ->
                            customizations[customizationId]!!.name == customization.name
                        }
                }
            )
        }
    }

    @When(
        "I attempt to update a Customization with the Id {string} on children with the Id {string} in the Offer with the Id {string}"
    )
    fun iAttemptToUpdateACustomizationWithTheIdOnChildrenWithTheIdInTheOfferWithTheId(
        customizationIdString: String,
        optionIdString: String,
        offerIdString: String,
    ) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        val optionId = Id(UUID.fromString(optionIdString))
        val offerId = Id(UUID.fromString(offerIdString))

        cucumberContext.result = runCatching {
            updateCustomizationOnChildrenInputPort.execute(cucumberContext.storeId, offerId, optionId, customizations[customizationId]!!)
        }.onFailure { it.printStackTrace() }
    }

    @Then("the Customization with the Id {string} should be updated in the offer")
    fun theCustomizationWithTheIdShouldBeUpdatedInTheOffer(customizationIdString: String) {
        val customizationId = Id(UUID.fromString(customizationIdString))
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer ->
                    offer.findCustomizationInChildrenById(customizationId)?.id == customizations[customizationId]?.id &&
                        offer.findCustomizationInChildrenById(customizationId)?.options?.toSet() == customizations[customizationId]?.options?.toSet()
                }
            )
        }
    }

    @When(
        "I attempt to update an Option with the Id {string} from parent Customization with the Id {string} in the Offer with the Id {string}"
    )
    fun iAttemptToUpdateAnOptionWithTheIdFromParentCustomizationWithTheIdInTheOfferWithTheId(
        optionIdString: String,
        customizationIdString: String,
        offerIdString: String,
    ) {
        val optionId = Id(UUID.fromString(optionIdString))
        val customizationId = Id(UUID.fromString(customizationIdString))
        val offerId = Id(UUID.fromString(offerIdString))

        cucumberContext.result = runCatching {
            updateOptionOnChildrenInputPort.execute(cucumberContext.storeId, offerId, customizationId, options[optionId]!!)
        }.onFailure { it.printStackTrace() }
    }

    @Then("the Option with the Id {string} should be updated in the offer")
    fun theOptionWithTheIdShouldBeUpdatedInTheOffer(optionIdString: String) {
        val optionId = Id(UUID.fromString(optionIdString))
        cucumberContext.result.exceptionOrNull()?.printStackTrace()
        cucumberContext.result.isSuccess shouldBe true

        verifySequence {
            cucumberContext.storeDatastoreOutputPort.exists(cucumberContext.storeId)

            cucumberContext.offerDatastoreOutputPort.findById(cucumberContext.storeId, offer.id)

            cucumberContext.offerDatastoreOutputPort.update(
                cucumberContext.storeId,
                match { offer ->
                    offer.findOptionInChildrenById(optionId) == options[optionId]
                }
            )
        }
    }
}
