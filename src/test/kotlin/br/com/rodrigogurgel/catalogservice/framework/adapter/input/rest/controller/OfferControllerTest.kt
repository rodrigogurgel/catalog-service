package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.AddOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CreateOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.DeleteOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.RemoveOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateCustomizationOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateCustomizationUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOptionOnChildrenUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOffer
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization.CustomizationRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization.UpdateCustomizationRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.CreateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.offer.UpdateOfferRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.option.OptionRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.option.UpdateOptionRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toResponseDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.math.BigDecimal
import java.util.UUID

@WebMvcTest(controllers = [OfferController::class])
class OfferControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = jacksonObjectMapper()

    @MockkBean
    private lateinit var createOfferUseCase: CreateOfferUseCase

    @MockkBean
    private lateinit var getOfferUseCase: GetOfferUseCase

    @MockkBean
    private lateinit var deleteOfferUseCase: DeleteOfferUseCase

    @MockkBean
    private lateinit var updateOfferUseCase: UpdateOfferUseCase

    @MockkBean
    private lateinit var addCustomizationUseCase: AddCustomizationUseCase

    @MockkBean
    private lateinit var updateCustomizationUseCase: UpdateCustomizationUseCase

    @MockkBean
    private lateinit var removeCustomizationUseCase: RemoveCustomizationUseCase

    @MockkBean
    private lateinit var addCustomizationOnChildrenUseCase: AddCustomizationOnChildrenUseCase

    @MockkBean
    private lateinit var updateCustomizationOnChildrenUseCase: UpdateCustomizationOnChildrenUseCase

    @MockkBean
    private lateinit var removeCustomizationOnChildrenUseCase: RemoveCustomizationOnChildrenUseCase

    @MockkBean
    private lateinit var addOptionOnChildrenUseCase: AddOptionOnChildrenUseCase

    @MockkBean
    private lateinit var updateOptionOnChildrenUseCase: UpdateOptionOnChildrenUseCase

    @MockkBean
    private lateinit var removeOptionOnChildrenUseCase: RemoveOptionOnChildrenUseCase

    @Test
    fun `Get offer`() {
        // given
        val storeId = UUID.randomUUID()
        val offer = mockOffer()
        val body = offer.toResponseDTO()

        every { getOfferUseCase.execute(Id(storeId), offer.id) } returns offer

        // when
        val result = mockMvc.get("/offers/{offerId}", offer.id.value.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(body.id.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.status", `is`(body.status.name))
            jsonPath("$.price", `is`(body.price.toDouble()))
            body.customizations.forEachIndexed { customizationIndex, customization ->
                jsonPath("$.customizations[$customizationIndex].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$customizationIndex].name", `is`(customization.name))
                jsonPath("$.customizations[$customizationIndex].description", `is`(customization.description))
                jsonPath("$.customizations[$customizationIndex].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$customizationIndex].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$customizationIndex].status", `is`(customization.status.name))
                customization.options.forEachIndexed { optionIndex, option ->
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].id",
                        `is`(option.id.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].productId",
                        `is`(option.productId.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].minPermitted",
                        `is`(option.minPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].maxPermitted",
                        `is`(option.maxPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].price",
                        `is`(option.price.toDouble())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].status",
                        `is`(option.status.name)
                    )
                }
            }
        }
    }

    @Test
    fun `Create Offer`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateOfferRequestDTO(
            id = UUID.randomUUID(),
            categoryId = UUID.randomUUID(),
            name = randomString(30),
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            status = Status.AVAILABLE,
            customizations = listOf(
                CustomizationRequestDTO(
                    id = UUID.randomUUID(),
                    name = randomString(50),
                    description = randomString(1000),
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    options = listOf(
                        OptionRequestDTO(
                            id = UUID.randomUUID(),
                            productId = UUID.randomUUID(),
                            price = BigDecimal.ONE,
                            minPermitted = 0,
                            maxPermitted = 1,
                            status = Status.AVAILABLE,
                            customizations = listOf(
                                CustomizationRequestDTO(
                                    id = UUID.randomUUID(),
                                    name = randomString(50),
                                    description = randomString(1000),
                                    minPermitted = 0,
                                    maxPermitted = 1,
                                    status = Status.AVAILABLE,
                                    options = listOf(
                                        OptionRequestDTO(
                                            id = UUID.randomUUID(),
                                            productId = UUID.randomUUID(),
                                            price = BigDecimal.ONE,
                                            minPermitted = 0,
                                            maxPermitted = 1,
                                            status = Status.AVAILABLE,
                                            customizations = listOf()
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        justRun { createOfferUseCase.execute(Id(storeId), Id(body.categoryId), body.toEntity()) }

        // when
        val result = mockMvc.post("/offers") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isCreated() }
        }.andExpectAll {
            jsonPath("$.id", `is`(body.id.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.status", `is`(body.status.name))
            jsonPath("$.price", `is`(body.price.toDouble()))
            body.customizations?.forEachIndexed { customizationIndex, customization ->
                jsonPath("$.customizations[$customizationIndex].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$customizationIndex].name", `is`(customization.name))
                jsonPath("$.customizations[$customizationIndex].description", `is`(customization.description))
                jsonPath("$.customizations[$customizationIndex].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$customizationIndex].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$customizationIndex].status", `is`(customization.status.name))
                customization.options.forEachIndexed { optionIndex, option ->
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].id",
                        `is`(option.id.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].productId",
                        `is`(option.productId.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].minPermitted",
                        `is`(option.minPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].maxPermitted",
                        `is`(option.maxPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].price",
                        `is`(option.price.toDouble())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].status",
                        `is`(option.status.name)
                    )
                }
            }
        }
    }

    @Test
    fun `Create Offer with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateOfferRequestDTO(
            categoryId = UUID.randomUUID(),
            name = randomString(30),
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            status = Status.AVAILABLE,
            customizations = listOf(
                CustomizationRequestDTO(
                    name = randomString(50),
                    description = null,
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    options = listOf(
                        OptionRequestDTO(
                            productId = UUID.randomUUID(),
                            price = BigDecimal.ONE,
                            minPermitted = 0,
                            maxPermitted = 1,
                            status = Status.AVAILABLE
                        )
                    )
                )
            )
        )

        justRun { createOfferUseCase.execute(Id(storeId), Id(body.categoryId), body.toEntity()) }

        // when
        val result = mockMvc.post("/offers") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isCreated() }
        }.andExpectAll {
            jsonPath("$.id", `is`(body.id.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.status", `is`(body.status.name))
            jsonPath("$.price", `is`(body.price.toDouble()))
            body.customizations?.forEachIndexed { customizationIndex, customization ->
                jsonPath("$.customizations[$customizationIndex].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$customizationIndex].name", `is`(customization.name))
                jsonPath("$.customizations[$customizationIndex].description", `is`(customization.description))
                jsonPath("$.customizations[$customizationIndex].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$customizationIndex].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$customizationIndex].status", `is`(customization.status.name))
                customization.options.forEachIndexed { optionIndex, option ->
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].id",
                        `is`(option.id.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].productId",
                        `is`(option.productId.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].minPermitted",
                        `is`(option.minPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].maxPermitted",
                        `is`(option.maxPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].price",
                        `is`(option.price.toDouble())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].status",
                        `is`(option.status.name)
                    )
                }
            }
        }
    }

    @Test
    fun `Create Offer without customization`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateOfferRequestDTO(
            categoryId = UUID.randomUUID(),
            name = randomString(30),
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            status = Status.AVAILABLE,
        )

        justRun { createOfferUseCase.execute(Id(storeId), Id(body.categoryId), body.toEntity()) }

        // when
        val result = mockMvc.post("/offers") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isCreated() }
        }.andExpectAll {
            jsonPath("$.id", `is`(body.id.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.status", `is`(body.status.name))
            jsonPath("$.price", `is`(body.price.toDouble()))
            body.customizations?.forEachIndexed { customizationIndex, customization ->
                jsonPath("$.customizations[$customizationIndex].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$customizationIndex].name", `is`(customization.name))
                jsonPath("$.customizations[$customizationIndex].description", `is`(customization.description))
                jsonPath("$.customizations[$customizationIndex].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$customizationIndex].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$customizationIndex].status", `is`(customization.status.name))
                customization.options.forEachIndexed { optionIndex, option ->
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].id",
                        `is`(option.id.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].productId",
                        `is`(option.productId.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].minPermitted",
                        `is`(option.minPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].maxPermitted",
                        `is`(option.maxPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].price",
                        `is`(option.price.toDouble())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].status",
                        `is`(option.status.name)
                    )
                }
            }
        }
    }

    @Test
    fun `Update Offer`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val body = UpdateOfferRequestDTO(
            categoryId = UUID.randomUUID(),
            name = randomString(30),
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            status = Status.AVAILABLE,
            customizations = listOf(
                CustomizationRequestDTO(
                    id = UUID.randomUUID(),
                    name = randomString(50),
                    description = randomString(1000),
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    options = listOf(
                        OptionRequestDTO(
                            id = UUID.randomUUID(),
                            productId = UUID.randomUUID(),
                            price = BigDecimal.ONE,
                            minPermitted = 0,
                            maxPermitted = 1,
                            status = Status.AVAILABLE,
                            customizations = listOf(
                                CustomizationRequestDTO(
                                    id = UUID.randomUUID(),
                                    name = randomString(50),
                                    description = randomString(1000),
                                    minPermitted = 0,
                                    maxPermitted = 1,
                                    status = Status.AVAILABLE,
                                    options = listOf(
                                        OptionRequestDTO(
                                            id = UUID.randomUUID(),
                                            productId = UUID.randomUUID(),
                                            price = BigDecimal.ONE,
                                            minPermitted = 0,
                                            maxPermitted = 1,
                                            status = Status.AVAILABLE,
                                            customizations = listOf()
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )

        justRun { updateOfferUseCase.execute(Id(storeId), body.toEntity(offerId)) }

        // when
        val result = mockMvc.put("/offers/{offerId}", offerId.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(offerId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.status", `is`(body.status.name))
            jsonPath("$.price", `is`(body.price.toDouble()))
            body.customizations?.forEachIndexed { customizationIndex, customization ->
                jsonPath("$.customizations[$customizationIndex].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$customizationIndex].name", `is`(customization.name))
                jsonPath("$.customizations[$customizationIndex].description", `is`(customization.description))
                jsonPath("$.customizations[$customizationIndex].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$customizationIndex].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$customizationIndex].status", `is`(customization.status.name))
                customization.options.forEachIndexed { optionIndex, option ->
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].id",
                        `is`(option.id.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].productId",
                        `is`(option.productId.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].minPermitted",
                        `is`(option.minPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].maxPermitted",
                        `is`(option.maxPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].price",
                        `is`(option.price.toDouble())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].status",
                        `is`(option.status.name)
                    )
                }
            }
        }
    }

    @Test
    fun `Update Offer with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val body = UpdateOfferRequestDTO(
            categoryId = UUID.randomUUID(),
            name = randomString(30),
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            status = Status.AVAILABLE,
            customizations = listOf(
                CustomizationRequestDTO(
                    name = randomString(50),
                    description = null,
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    options = listOf(
                        OptionRequestDTO(
                            productId = UUID.randomUUID(),
                            price = BigDecimal.ONE,
                            minPermitted = 0,
                            maxPermitted = 1,
                            status = Status.AVAILABLE,
                            customizations = null
                        )
                    )
                )
            )
        )

        justRun { updateOfferUseCase.execute(Id(storeId), body.toEntity(offerId)) }

        // when
        val result = mockMvc.put("/offers/{offerId}", offerId.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(offerId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.status", `is`(body.status.name))
            jsonPath("$.price", `is`(body.price.toDouble()))
            body.customizations?.forEachIndexed { customizationIndex, customization ->
                jsonPath("$.customizations[$customizationIndex].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$customizationIndex].name", `is`(customization.name))
                jsonPath("$.customizations[$customizationIndex].description", `is`(customization.description))
                jsonPath("$.customizations[$customizationIndex].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$customizationIndex].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$customizationIndex].status", `is`(customization.status.name))
                customization.options.forEachIndexed { optionIndex, option ->
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].id",
                        `is`(option.id.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].productId",
                        `is`(option.productId.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].minPermitted",
                        `is`(option.minPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].maxPermitted",
                        `is`(option.maxPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].price",
                        `is`(option.price.toDouble())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].status",
                        `is`(option.status.name)
                    )
                }
            }
        }
    }

    @Test
    fun `Update Offer without customizations`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val body = UpdateOfferRequestDTO(
            categoryId = UUID.randomUUID(),
            name = randomString(30),
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            status = Status.AVAILABLE,
        )

        justRun { updateOfferUseCase.execute(Id(storeId), body.toEntity(offerId)) }

        // when
        val result = mockMvc.put("/offers/{offerId}", offerId.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(offerId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.status", `is`(body.status.name))
            jsonPath("$.price", `is`(body.price.toDouble()))
            body.customizations?.forEachIndexed { customizationIndex, customization ->
                jsonPath("$.customizations[$customizationIndex].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$customizationIndex].name", `is`(customization.name))
                jsonPath("$.customizations[$customizationIndex].description", `is`(customization.description))
                jsonPath("$.customizations[$customizationIndex].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$customizationIndex].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$customizationIndex].status", `is`(customization.status.name))
                customization.options.forEachIndexed { optionIndex, option ->
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].id",
                        `is`(option.id.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].productId",
                        `is`(option.productId.toString())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].minPermitted",
                        `is`(option.minPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].maxPermitted",
                        `is`(option.maxPermitted)
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].price",
                        `is`(option.price.toDouble())
                    )
                    jsonPath(
                        "$.customizations[$customizationIndex].options[$optionIndex].status",
                        `is`(option.status.name)
                    )
                }
            }
        }
    }

    @Test
    fun `Delete Offer`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()

        justRun { deleteOfferUseCase.execute(Id(storeId), Id(offerId)) }

        // when
        val result = mockMvc.delete("/offers/{offerId}", offerId.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // then
        result.andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun `Add Customization`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val body = CustomizationRequestDTO(
            name = randomString(50),
            description = null,
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
            options = listOf(
                OptionRequestDTO(
                    productId = UUID.randomUUID(),
                    price = BigDecimal.ONE,
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    customizations = null
                )
            )
        )

        justRun { addCustomizationUseCase.execute(Id(storeId), Id(offerId), body.toEntity()) }

        // when
        val result = mockMvc.post("/offers/{offerId}/customizations", offerId.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isCreated() }
        }.andExpectAll {
            jsonPath("$.id", `is`(body.id.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.status", `is`(body.status.name))
            body.options.forEachIndexed { optionIndex, option ->
                jsonPath("$.options[$optionIndex].id", `is`(option.id.toString()))
                jsonPath("$.options[$optionIndex].productId", `is`(option.productId.toString()))
                jsonPath("$.options[$optionIndex].minPermitted", `is`(option.minPermitted))
                jsonPath("$.options[$optionIndex].maxPermitted", `is`(option.maxPermitted))
                jsonPath("$.options[$optionIndex].price", `is`(option.price.toDouble()))
                jsonPath("$.options[$optionIndex].status", `is`(option.status.name))
            }
        }
    }

    @Test
    fun `Update Customization`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()
        val body = UpdateCustomizationRequestDTO(
            name = randomString(50),
            description = randomString(1000),
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
            options = listOf(
                OptionRequestDTO(
                    productId = UUID.randomUUID(),
                    price = BigDecimal.ONE,
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    customizations = null
                )
            )
        )

        justRun { updateCustomizationUseCase.execute(Id(storeId), Id(offerId), body.toEntity(customizationId)) }

        // when
        val result = mockMvc.put(
            "/offers/{offerId}/customizations/{customizationId}",
            offerId.toString(),
            customizationId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(customizationId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.status", `is`(body.status.name))
            body.options.forEachIndexed { optionIndex, option ->
                jsonPath("$.options[$optionIndex].id", `is`(option.id.toString()))
                jsonPath("$.options[$optionIndex].productId", `is`(option.productId.toString()))
                jsonPath("$.options[$optionIndex].minPermitted", `is`(option.minPermitted))
                jsonPath("$.options[$optionIndex].maxPermitted", `is`(option.maxPermitted))
                jsonPath("$.options[$optionIndex].price", `is`(option.price.toDouble()))
                jsonPath("$.options[$optionIndex].status", `is`(option.status.name))
            }
        }
    }

    @Test
    fun `Update Customization with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()
        val body = UpdateCustomizationRequestDTO(
            name = randomString(50),
            description = null,
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
            options = listOf(
                OptionRequestDTO(
                    productId = UUID.randomUUID(),
                    price = BigDecimal.ONE,
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    customizations = null
                )
            )
        )

        justRun { updateCustomizationUseCase.execute(Id(storeId), Id(offerId), body.toEntity(customizationId)) }

        // when
        val result = mockMvc.put(
            "/offers/{offerId}/customizations/{customizationId}",
            offerId.toString(),
            customizationId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(customizationId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.status", `is`(body.status.name))
            body.options.forEachIndexed { optionIndex, option ->
                jsonPath("$.options[$optionIndex].id", `is`(option.id.toString()))
                jsonPath("$.options[$optionIndex].productId", `is`(option.productId.toString()))
                jsonPath("$.options[$optionIndex].minPermitted", `is`(option.minPermitted))
                jsonPath("$.options[$optionIndex].maxPermitted", `is`(option.maxPermitted))
                jsonPath("$.options[$optionIndex].price", `is`(option.price.toDouble()))
                jsonPath("$.options[$optionIndex].status", `is`(option.status.name))
            }
        }
    }

    @Test
    fun `Remove Customization`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()

        justRun { removeCustomizationUseCase.execute(Id(storeId), Id(offerId), Id(customizationId)) }

        // when
        val result = mockMvc.delete(
            "/offers/{offerId}/customizations/{customizationId}",
            offerId.toString(),
            customizationId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // then
        result.andExpect { status { isNoContent() } }
    }

    @Test
    fun `Add Customization on Children`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val optionId = UUID.randomUUID()
        val body = CustomizationRequestDTO(
            name = randomString(50),
            description = null,
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
            options = listOf(
                OptionRequestDTO(
                    productId = UUID.randomUUID(),
                    price = BigDecimal.ONE,
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    customizations = null
                )
            )
        )

        justRun { addCustomizationOnChildrenUseCase.execute(Id(storeId), Id(offerId), Id(optionId), body.toEntity()) }

        // when
        val result = mockMvc.post(
            "/offers/{offerId}/options/{optionId}/customizations",
            offerId.toString(),
            optionId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isCreated() }
        }.andExpectAll {
            jsonPath("$.id", `is`(body.id.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.status", `is`(body.status.name))
            body.options.forEachIndexed { optionIndex, option ->
                jsonPath("$.options[$optionIndex].id", `is`(option.id.toString()))
                jsonPath("$.options[$optionIndex].productId", `is`(option.productId.toString()))
                jsonPath("$.options[$optionIndex].minPermitted", `is`(option.minPermitted))
                jsonPath("$.options[$optionIndex].maxPermitted", `is`(option.maxPermitted))
                jsonPath("$.options[$optionIndex].price", `is`(option.price.toDouble()))
                jsonPath("$.options[$optionIndex].status", `is`(option.status.name))
            }
        }
    }

    @Test
    fun `Update Customization on Children`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val optionId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()
        val body = UpdateCustomizationRequestDTO(
            name = randomString(50),
            description = randomString(1000),
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
            options = listOf(
                OptionRequestDTO(
                    productId = UUID.randomUUID(),
                    price = BigDecimal.ONE,
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    customizations = null
                )
            )
        )

        justRun {
            updateCustomizationOnChildrenUseCase.execute(
                Id(storeId),
                Id(offerId),
                Id(optionId),
                body.toEntity(customizationId)
            )
        }

        // when
        val result = mockMvc.put(
            "/offers/{offerId}/options/{optionId}/customizations/{customizationId}",
            offerId.toString(),
            optionId.toString(),
            customizationId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(customizationId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.status", `is`(body.status.name))
            body.options.forEachIndexed { optionIndex, option ->
                jsonPath("$.options[$optionIndex].id", `is`(option.id.toString()))
                jsonPath("$.options[$optionIndex].productId", `is`(option.productId.toString()))
                jsonPath("$.options[$optionIndex].minPermitted", `is`(option.minPermitted))
                jsonPath("$.options[$optionIndex].maxPermitted", `is`(option.maxPermitted))
                jsonPath("$.options[$optionIndex].price", `is`(option.price.toDouble()))
                jsonPath("$.options[$optionIndex].status", `is`(option.status.name))
            }
        }
    }

    @Test
    fun `Update Customization on Children with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val optionId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()
        val body = UpdateCustomizationRequestDTO(
            name = randomString(50),
            description = null,
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
            options = listOf(
                OptionRequestDTO(
                    productId = UUID.randomUUID(),
                    price = BigDecimal.ONE,
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    customizations = null
                )
            )
        )

        justRun {
            updateCustomizationOnChildrenUseCase.execute(
                Id(storeId),
                Id(offerId),
                Id(optionId),
                body.toEntity(customizationId)
            )
        }

        // when
        val result = mockMvc.put(
            "/offers/{offerId}/options/{optionId}/customizations/{customizationId}",
            offerId.toString(),
            optionId.toString(),
            customizationId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(customizationId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.status", `is`(body.status.name))
            body.options.forEachIndexed { optionIndex, option ->
                jsonPath("$.options[$optionIndex].id", `is`(option.id.toString()))
                jsonPath("$.options[$optionIndex].productId", `is`(option.productId.toString()))
                jsonPath("$.options[$optionIndex].minPermitted", `is`(option.minPermitted))
                jsonPath("$.options[$optionIndex].maxPermitted", `is`(option.maxPermitted))
                jsonPath("$.options[$optionIndex].price", `is`(option.price.toDouble()))
                jsonPath("$.options[$optionIndex].status", `is`(option.status.name))
            }
        }
    }

    @Test
    fun `Remove Customization on Children`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val optionId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()

        justRun {
            removeCustomizationOnChildrenUseCase.execute(
                Id(storeId),
                Id(offerId),
                Id(optionId),
                Id(customizationId)
            )
        }

        // when
        val result = mockMvc.delete(
            "/offers/{offerId}/options/{optionId}/customizations/{customizationId}",
            offerId.toString(),
            optionId.toString(),
            customizationId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // then
        result.andExpect { status { isNoContent() } }
    }

    @Test
    fun `Add Option`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()
        val body = OptionRequestDTO(
            id = UUID.randomUUID(),
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
        )

        justRun { addOptionOnChildrenUseCase.execute(Id(storeId), Id(offerId), Id(customizationId), body.toEntity()) }

        // when
        val result = mockMvc.post(
            "/offers/{offerId}/customizations/{customizationId}/options",
            offerId.toString(),
            customizationId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isCreated() }
        }.andExpectAll {
            jsonPath("$.id", `is`(body.id.toString()))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.price", `is`(body.price.toDouble()))
            jsonPath("$.status", `is`(body.status.name))
            body.customizations?.forEachIndexed { index, customization ->
                jsonPath("$.customizations[$index].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$index].name", `is`(customization.name))
                jsonPath("$.customizations[$index].description", `is`(customization.description))
                jsonPath("$.customizations[$index].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$index].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$index].status", `is`(customization.status.name))
            }
        }
    }

    @Test
    fun `Update Option`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()
        val optionId = UUID.randomUUID()
        val body = UpdateOptionRequestDTO(
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
            customizations = listOf(
                CustomizationRequestDTO(
                    name = randomString(50),
                    description = randomString(1000),
                    minPermitted = 0,
                    maxPermitted = 1,
                    status = Status.AVAILABLE,
                    options = listOf(
                        OptionRequestDTO(
                            productId = UUID.randomUUID(),
                            price = BigDecimal.ONE,
                            minPermitted = 0,
                            maxPermitted = 1,
                            status = Status.AVAILABLE,
                            customizations = null
                        )
                    )
                )
            )
        )

        justRun {
            updateOptionOnChildrenUseCase.execute(
                Id(storeId),
                Id(offerId),
                Id(customizationId),
                body.toEntity(optionId)
            )
        }

        // when
        val result = mockMvc.put(
            "/offers/{offerId}/customizations/{customizationId}/options/{optionId}",
            offerId.toString(),
            customizationId.toString(),
            optionId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(optionId.toString()))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.price", `is`(body.price.toDouble()))
            jsonPath("$.status", `is`(body.status.name))
            body.customizations?.forEachIndexed { index, customization ->
                jsonPath("$.customizations[$index].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$index].name", `is`(customization.name))
                jsonPath("$.customizations[$index].description", `is`(customization.description))
                jsonPath("$.customizations[$index].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$index].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$index].status", `is`(customization.status.name))
            }
        }
    }

    @Test
    fun `Update Option with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()
        val optionId = UUID.randomUUID()
        val body = UpdateOptionRequestDTO(
            productId = UUID.randomUUID(),
            price = BigDecimal.ONE,
            minPermitted = 0,
            maxPermitted = 1,
            status = Status.AVAILABLE,
        )

        justRun {
            updateOptionOnChildrenUseCase.execute(
                Id(storeId),
                Id(offerId),
                Id(customizationId),
                body.toEntity(optionId)
            )
        }

        // when
        val result = mockMvc.put(
            "/offers/{offerId}/customizations/{customizationId}/options/{optionId}",
            offerId.toString(),
            customizationId.toString(),
            optionId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(optionId.toString()))
            jsonPath("$.productId", `is`(body.productId.toString()))
            jsonPath("$.minPermitted", `is`(body.minPermitted))
            jsonPath("$.maxPermitted", `is`(body.maxPermitted))
            jsonPath("$.price", `is`(body.price.toDouble()))
            jsonPath("$.status", `is`(body.status.name))
            body.customizations?.forEachIndexed { index, customization ->
                jsonPath("$.customizations[$index].id", `is`(customization.id.toString()))
                jsonPath("$.customizations[$index].name", `is`(customization.name))
                jsonPath("$.customizations[$index].description", `is`(customization.description))
                jsonPath("$.customizations[$index].minPermitted", `is`(customization.minPermitted))
                jsonPath("$.customizations[$index].maxPermitted", `is`(customization.maxPermitted))
                jsonPath("$.customizations[$index].status", `is`(customization.status.name))
            }
        }
    }

    @Test
    fun `Remove Option on Children`() {
        // given
        val storeId = UUID.randomUUID()
        val offerId = UUID.randomUUID()
        val customizationId = UUID.randomUUID()
        val optionId = UUID.randomUUID()

        justRun { removeOptionOnChildrenUseCase.execute(Id(storeId), Id(offerId), Id(customizationId), Id(optionId)) }

        // when
        val result = mockMvc.delete(
            "/offers/{offerId}/customizations/{customizationId}/options/{optionId}",
            offerId.toString(),
            customizationId.toString(),
            optionId.toString()
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // then
        result.andExpect { status { isNoContent() } }
    }
}
