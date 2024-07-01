package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.config.DatabaseTestConfig
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product.CreateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product.UpdateProductRequestDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [DatabaseTestConfig::class])
class ProductControllerTest {
    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @BeforeEach
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build()
    }

    @Test
    fun `Should get Product successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateProductRequestDTO(
            name = randomString(30),
            description = null,
            image = null
        )

        // and
        mockMvc.post(
            "/products",
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.image").value(body.image)
            }

        // when
        val result = mockMvc.get("/products/{id}", body.id) {
            queryParam("storeId", storeId.toString())
        }
            .andDo { print() }
        // then
        result
            .andExpect {
                status { isOk() }
            }
            .andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.image").value(body.image)
            }
    }

    @Test
    fun `Should create a Product successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateProductRequestDTO(
            id = UUID.randomUUID(),
            name = randomString(30),
            description = randomString(1000),
            image = "https://www.image.com.br"
        )

        // when
        mockMvc.post(
            "/products",
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.image").value(body.image)
            }

        // then
        mockMvc.get("/products/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }.andExpect {
            MockMvcResultMatchers.jsonPath("$.id").value(body.id)
            MockMvcResultMatchers.jsonPath("$.name").value(body.name)
            MockMvcResultMatchers.jsonPath("$.description").value(body.description)
            MockMvcResultMatchers.jsonPath("$.image").value(body.image)
        }
    }

    @Test
    fun `Should update a Product successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateProductRequestDTO(
            id = UUID.randomUUID(),
            name = randomString(30),
            description = randomString(1000),
            image = "https://www.image.com.br"
        )

        // and
        mockMvc.post(
            "/products",
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.image").value(body.image)
            }

        // when
        val updatedBody = body.run {
            UpdateProductRequestDTO(name, randomString(1000), "https://www.String")
        }
        mockMvc.put(
            "/products/{id}",
            body.id
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updatedBody)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }

        // then
        mockMvc.get("/products/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            MockMvcResultMatchers.jsonPath("$.id").value(body.id)
            MockMvcResultMatchers.jsonPath("$.name").value(updatedBody.name)
            MockMvcResultMatchers.jsonPath("$.description").value(updatedBody.description)
            MockMvcResultMatchers.jsonPath("$.image").value(updatedBody.image)
        }
    }

    @Test
    fun `Should update a Product optional values successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateProductRequestDTO(
            id = UUID.randomUUID(),
            name = randomString(30),
            description = randomString(1000),
            image = "https://www.image.com.br"
        )

        // and
        mockMvc.post(
            "/products",
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.image").value(body.image)
            }

        // when
        val updatedBody = body.run {
            UpdateProductRequestDTO(name, null, null)
        }
        mockMvc.put(
            "/products/{id}",
            body.id
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updatedBody)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }

        // then
        mockMvc.get("/products/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            MockMvcResultMatchers.jsonPath("$.id").value(body.id)
            MockMvcResultMatchers.jsonPath("$.name").value(updatedBody.name)
            MockMvcResultMatchers.jsonPath("$.description").value(updatedBody.description)
            MockMvcResultMatchers.jsonPath("$.image").value(updatedBody.image)
        }
    }

    @Test
    fun `Should delete a Product successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateProductRequestDTO(
            id = UUID.randomUUID(),
            name = randomString(30),
            description = randomString(1000),
            image = "https://www.image.com.br"
        )

        // and
        mockMvc.post(
            "/products",
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.image").value(body.image)
            }

        // when
        mockMvc.delete(
            "/products/{id}",
            body.id
        ) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }

        // then
        mockMvc.get("/products/{id}", body.id) {
            queryParam("storeId", storeId.toString())
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound() }
            }
            .andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.image").value(body.image)
            }
    }

    @Test
    fun `Should get a empty Page of Products Successfully`() {
        val storeId = UUID.randomUUID()

        mockMvc.get("/products") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.total").value(0)
            }
    }

    @Test
    fun `Should get a Page of Products successfully`() {
        // given
        val requests = List(10) {
            CreateProductRequestDTO(
                id = UUID.randomUUID(),
                name = randomString(30),
                description = randomString(1000),
                image = "https://www.image.com.br"
            )
        }.sortedBy { body -> body.name }
        val storeId = UUID.randomUUID()

        // and
        requests.forEach { body ->
            mockMvc.post(
                "/products",
            ) {
                queryParam("storeId", storeId.toString())
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                }.andExpect {
                    MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                    MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                    MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                    MockMvcResultMatchers.jsonPath("$.image").value(body.image)
                }
        }

        // when
        mockMvc.get("/products") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                requests.forEachIndexed { index, body ->
                    MockMvcResultMatchers.jsonPath("$.data.$index.id").value(body.id)
                    MockMvcResultMatchers.jsonPath("$.data.$index.name").value(body.name)
                    MockMvcResultMatchers.jsonPath("$.data.$index.description").value(body.description)
                    MockMvcResultMatchers.jsonPath("$.data.$index.image").value(body.image)
                }
            }
    }

    @Test
    fun `Should get a Page of Products filtered by name successfully`() {
        // given
        val nopeProducts = List(10) {
            CreateProductRequestDTO(
                id = UUID.randomUUID(),
                name = "NOPE" + randomString(20),
                description = randomString(1000),
                image = "https://www.image.com.br"
            )
        }
        val yesProducts = List(3) {
            CreateProductRequestDTO(
                id = UUID.randomUUID(),
                name = "YES" + randomString(20),
                description = randomString(1000),
                image = "https://www.image.com.br"
            )
        }

        val requests = (nopeProducts + yesProducts).sortedBy { body -> body.name }
        val storeId = UUID.randomUUID()

        // and
        requests.forEach { body ->
            mockMvc.post(
                "/products",
            ) {
                queryParam("storeId", storeId.toString())
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                }.andExpect {
                    MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                    MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                    MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                    MockMvcResultMatchers.jsonPath("$.image").value(body.image)
                }
        }

        // when
        mockMvc.get("/products") {
            queryParam("storeId", storeId.toString())
            queryParam("beginsWith", "YES")
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                yesProducts.forEachIndexed { index, body ->
                    MockMvcResultMatchers.jsonPath("$.data.$index.id").value(body.id)
                    MockMvcResultMatchers.jsonPath("$.data.$index.name").value(body.name)
                    MockMvcResultMatchers.jsonPath("$.data.$index.description").value(body.description)
                    MockMvcResultMatchers.jsonPath("$.data.$index.image").value(body.image)
                }
            }
    }
}
