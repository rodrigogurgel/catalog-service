package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.category.CountCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CreateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.DeleteCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.UpdateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CountOffersUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOffersUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCategory
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockCustomization
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockOfferWith
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.CreateCategoryRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.UpdateCategoryRequestDTO
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
import java.util.UUID

@WebMvcTest(controllers = [CategoryController::class])
class CategoryControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper = jacksonObjectMapper()

    @MockkBean
    private lateinit var getCategoryUseCase: GetCategoryUseCase

    @MockkBean
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @MockkBean
    private lateinit var createCategoryUseCase: CreateCategoryUseCase

    @MockkBean
    private lateinit var updateCategoryUseCase: UpdateCategoryUseCase

    @MockkBean
    private lateinit var deleteCategoryUseCase: DeleteCategoryUseCase

    @MockkBean
    private lateinit var countCategoriesUseCase: CountCategoriesUseCase

    @MockkBean
    private lateinit var countOffersUseCase: CountOffersUseCase

    @MockkBean
    private lateinit var getOffersUseCase: GetOffersUseCase

    @Test
    fun `Get Category`() {
        // given
        val storeId = UUID.randomUUID()
        val category = mockCategory()

        every { getCategoryUseCase.execute(Id(storeId), category.id) } returns category

        // when
        val result = mockMvc.get("/categories/{categoryId}", category.id.value.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(category.id.value.toString()))
            jsonPath("$.name", `is`(category.name.value))
            jsonPath("$.description", `is`(category.description?.value))
            jsonPath("$.status", `is`(category.status.name))
        }
    }

    @Test
    fun `Create Category`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateCategoryRequestDTO(
            id = UUID.randomUUID(),
            name = randomString(30),
            description = randomString(1000),
            status = Status.AVAILABLE
        )

        justRun { createCategoryUseCase.execute(Id(storeId), body.toEntity()) }

        // when
        val result = mockMvc.post("/categories") {
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
            jsonPath("$.status", `is`(body.status.name))
        }
    }

    @Test
    fun `Create Category with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateCategoryRequestDTO(
            name = randomString(30),
            description = null,
            status = Status.AVAILABLE
        )

        justRun { createCategoryUseCase.execute(Id(storeId), body.toEntity()) }

        // when
        val result = mockMvc.post("/categories") {
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
            jsonPath("$.status", `is`(body.status.name))
        }
    }

    @Test
    fun `Update Category`() {
        // given
        val storeId = UUID.randomUUID()
        val categoryId = UUID.randomUUID()
        val body = UpdateCategoryRequestDTO(
            name = randomString(30),
            description = randomString(1000),
            status = Status.AVAILABLE
        )

        justRun { updateCategoryUseCase.execute(Id(storeId), body.toEntity(categoryId)) }

        // when
        val result = mockMvc.put("/categories/{id}", categoryId) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(categoryId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.status", `is`(body.status.name))
        }
    }

    @Test
    fun `Update Category with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val categoryId = UUID.randomUUID()
        val body = UpdateCategoryRequestDTO(
            name = randomString(30),
            description = null,
            status = Status.AVAILABLE
        )

        justRun { updateCategoryUseCase.execute(Id(storeId), body.toEntity(categoryId)) }

        // when
        val result = mockMvc.put("/categories/{id}", categoryId) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.id", `is`(categoryId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.status", `is`(body.status.name))
        }
    }

    @Test
    fun `Delete Category`() {
        // given
        val storeId = UUID.randomUUID()
        val categoryId = UUID.randomUUID()

        justRun { deleteCategoryUseCase.execute(Id(storeId), Id(categoryId)) }

        // when
        val result = mockMvc.delete("/categories/{id}", categoryId) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        result.andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun `Get Page of Categories`() {
        // given
        val storeId = UUID.randomUUID()
        val categories = List(10) {
            mockCategory()
        }.sortedBy { it.name.value }

        every { countCategoriesUseCase.execute(Id(storeId), null) } returns categories.size
        every { getCategoriesUseCase.execute(Id(storeId), 20, 0, null) } returns categories

        // when
        val result = mockMvc.get("/categories") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // when
        result.andExpect {
            status { isOk() }
        }
            .andExpectAll {
                jsonPath("$.limit", `is`(20))
                jsonPath("$.offset", `is`(0))
                jsonPath("$.total", `is`(10))
                categories.map { it.toResponseDTO() }.sortedBy { body -> body.name }.forEachIndexed { index, body ->
                    jsonPath("$.data[$index].id", `is`(body.id.toString()))
                    jsonPath("$.data[$index].name", `is`(body.name))
                    jsonPath("$.data[$index].description", `is`(body.description))
                    jsonPath("$.data[$index].status", `is`(body.status.name))
                }
            }
    }

    @Test
    fun `Get Offers by Category Id`() {
        // given
        val storeId = UUID.randomUUID()
        val categoryId = UUID.randomUUID()
        val offers = List(10) {
            mockOfferWith {
                customizations = mutableListOf(mockCustomization(), mockCustomization())
            }
        }.sortedBy { it.name.value }

        every { getOffersUseCase.execute(Id(storeId), Id(categoryId), 20, 0, null) } returns offers
        every { countOffersUseCase.execute(Id(storeId), Id(categoryId), null) } returns offers.size

        // when
        val result = mockMvc.get("/categories/{categoryId}/offers", categoryId.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        result.andExpect {
            status { isOk() }
        }.andExpectAll {
            jsonPath("$.limit", `is`(20))
            jsonPath("$.offset", `is`(0))
            jsonPath("$.total", `is`(10))
            offers.map { it.toResponseDTO() }.sortedBy { body -> body.name }.forEachIndexed { index, offer ->
                jsonPath("$.data[$index].id", `is`(offer.id.toString()))
                jsonPath("$.data[$index].name", `is`(offer.name))
                jsonPath("$.data[$index].productId", `is`(offer.productId.toString()))
                jsonPath("$.data[$index].status", `is`(offer.status.name))
                jsonPath("$.data[$index].price", `is`(offer.price.toDouble()))
                offer.customizations.forEachIndexed { customizationIndex, customization ->
                    jsonPath("$.data[$index].customizations[$customizationIndex].id", `is`(customization.id.toString()))
                    jsonPath("$.data[$index].customizations[$customizationIndex].name", `is`(customization.name))
                    jsonPath(
                        "$.data[$index].customizations[$customizationIndex].description",
                        `is`(customization.description)
                    )
                    jsonPath(
                        "$.data[$index].customizations[$customizationIndex].minPermitted",
                        `is`(customization.minPermitted)
                    )
                    jsonPath(
                        "$.data[$index].customizations[$customizationIndex].maxPermitted",
                        `is`(customization.maxPermitted)
                    )
                    jsonPath(
                        "$.data[$index].customizations[$customizationIndex].status",
                        `is`(customization.status.name)
                    )
                    customization.options.forEachIndexed { optionIndex, option ->
                        jsonPath(
                            "$.data[$index].customizations[$customizationIndex].options[$optionIndex].id",
                            `is`(option.id.toString())
                        )
                        jsonPath(
                            "$.data[$index].customizations[$customizationIndex].options[$optionIndex].productId",
                            `is`(option.productId.toString())
                        )
                        jsonPath(
                            "$.data[$index].customizations[$customizationIndex].options[$optionIndex].minPermitted",
                            `is`(option.minPermitted)
                        )
                        jsonPath(
                            "$.data[$index].customizations[$customizationIndex].options[$optionIndex].maxPermitted",
                            `is`(option.maxPermitted)
                        )
                        jsonPath(
                            "$.data[$index].customizations[$customizationIndex].options[$optionIndex].price",
                            `is`(option.price.toDouble())
                        )
                        jsonPath(
                            "$.data[$index].customizations[$customizationIndex].options[$optionIndex].status",
                            `is`(option.status.name)
                        )
                    }
                }
            }
        }
    }
}
