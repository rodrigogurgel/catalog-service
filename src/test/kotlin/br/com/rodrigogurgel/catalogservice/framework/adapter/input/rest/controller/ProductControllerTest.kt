package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.product.CountProductsUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CreateProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductsUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.UpdateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.fixture.mock.mockProduct
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product.CreateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product.UpdateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toResponseDTO
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
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

@WebMvcTest(controllers = [ProductController::class])
class ProductControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var createProductUseCase: CreateProductUseCase

    @MockkBean
    private lateinit var updateProductUseCase: UpdateProductUseCase

    @MockkBean
    private lateinit var getProductUseCase: GetProductUseCase

    @MockkBean
    private lateinit var deleteProductUseCase: DeleteProductUseCase

    @MockkBean
    private lateinit var getProductsUseCase: GetProductsUseCase

    @MockkBean
    private lateinit var countProductsUseCase: CountProductsUseCase

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @Test
    fun `Create Product`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateProductRequestDTO(
            name = randomString(30),
            description = randomString(1000),
            imagePath = "https://image.com.br"
        )

        justRun { createProductUseCase.execute(Id(storeId), body.toEntity()) }

        // when
        val result = mockMvc.post(
            "/products",
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
            jsonPath("$.imagePath", `is`(body.imagePath))
        }

        verify(exactly = 1) {
            createProductUseCase.execute(Id(storeId), body.toEntity())
        }
    }

    @Test
    fun `Create Product with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateProductRequestDTO(
            name = randomString(30),
            description = null,
            imagePath = null
        )

        justRun { createProductUseCase.execute(Id(storeId), body.toEntity()) }

        // when
        val result = mockMvc.post(
            "/products",
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
            jsonPath("$.imagePath", `is`(body.imagePath))
        }
    }

    @Test
    fun `Get Product endpoint`() {
        // given
        val storeId = UUID.randomUUID()
        val product = mockProduct()
        every { getProductUseCase.execute(Id(storeId), product.id) } returns product

        // when
        val result = mockMvc.get("/products/{productId}", product.id.value.toString()) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }

        // then
        result.andExpect {
            status { isOk() }
        }.andExpect {
            jsonPath("$.id", `is`(product.id.value.toString()))
            jsonPath("$.name", `is`(product.name.value))
            jsonPath("$.description", `is`(product.description?.value))
            jsonPath("$.imagePath", `is`(product.image?.path))
        }
    }

    @Test
    fun `Update Product`() {
        // given
        val storeId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val body = UpdateProductRequestDTO(
            name = randomString(30),
            description = randomString(1000),
            imagePath = "https://www.image.com.br"
        )

        justRun { updateProductUseCase.execute(Id(storeId), body.toEntity(productId)) }

        // when
        val result = mockMvc.put(
            "/products/{id}",
            productId.toString()
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
            jsonPath("$.id", `is`(productId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.imagePath", `is`(body.imagePath))
        }
    }

    @Test
    fun `Update Product with optional values`() {
        // given
        val storeId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val body = UpdateProductRequestDTO(
            name = randomString(30),
            description = null,
            imagePath = null
        )

        justRun { updateProductUseCase.execute(Id(storeId), body.toEntity(productId)) }

        // when
        val result = mockMvc.put(
            "/products/{id}",
            productId.toString()
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
            jsonPath("$.id", `is`(productId.toString()))
            jsonPath("$.name", `is`(body.name))
            jsonPath("$.description", `is`(body.description))
            jsonPath("$.imagePath", `is`(body.imagePath))
        }
    }

    @Test
    fun `Delete Product`() {
        // given
        val storeId = UUID.randomUUID()
        val productId = UUID.randomUUID()

        justRun { deleteProductUseCase.execute(Id(storeId), Id(productId)) }

        // when
        val result = mockMvc.delete(
            "/products/{id}",
            productId.toString()
        ) {
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
    fun `Get Page of Products`() {
        val storeId = UUID.randomUUID()
        val products = List(10) {
            mockProduct()
        }.sortedBy { body -> body.name.value }

        every { countProductsUseCase.execute(Id(storeId), null) } returns products.size
        every { getProductsUseCase.execute(Id(storeId), 20, 0, null) } returns products

        // when
        mockMvc.get("/products") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                products
                    .map { it.toResponseDTO() }
                    .forEachIndexed { index, body ->
                        jsonPath("$.data[$index].id", `is`(body.id.toString()))
                        jsonPath("$.data[$index].name", `is`(body.name))
                        jsonPath("$.data[$index].description", `is`(body.description))
                        jsonPath("$.data[$index].imagePath", `is`(body.imagePath))
                    }
            }
    }
}
