package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.config.DatabaseTestConfig
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.CreateCategoryRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.UpdateCategoryRequestDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.Matchers.`is`
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [DatabaseTestConfig::class])
class CategoryControllerTest {
    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @BeforeEach
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build()
    }

    @Test
    fun `Should create a Category successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateCategoryRequestDTO(
            id = UUID.randomUUID(),
            name = randomString(30),
            description = randomString(1000),
            status = Status.AVAILABLE
        )

        // when
        mockMvc.post("/categories") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(body.name))
                jsonPath("$.description", `is`(body.description))
                jsonPath("$.status", `is`(body.status.name))
            }

        // then
        mockMvc.get("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(body.name))
                jsonPath("$.description", `is`(body.description))
                jsonPath("$.status", `is`(body.status.name))
            }
    }

    @Test
    fun `Should create a Category with optional values successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateCategoryRequestDTO(
            name = randomString(30),
            description = null,
            status = Status.AVAILABLE
        )

        // when
        mockMvc.post("/categories") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(body.name))
                jsonPath("$.description", `is`(body.description))
                jsonPath("$.status", `is`(body.status.name))
            }

        // then
        mockMvc.get("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(body.name))
                jsonPath("$.description", `is`(body.description))
                jsonPath("$.status", `is`(body.status.name))
            }
    }

    @Test
    fun `Should update a Category successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateCategoryRequestDTO(
            name = randomString(30),
            description = randomString(1000),
            status = Status.AVAILABLE
        )

        mockMvc.post("/categories") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(body.name))
                jsonPath("$.description", `is`(body.description))
                jsonPath("$.status", `is`(body.status.name))
            }

        val updatedBody = body.run {
            UpdateCategoryRequestDTO(
                name,
                randomString(30),
                Status.UNAVAILABLE
            )
        }

        // when
        mockMvc.put("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updatedBody)
        }.andExpect {
            status { isOk() }
        }

        // then
        mockMvc.get("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(updatedBody.name))
                jsonPath("$.description", `is`(updatedBody.description))
                jsonPath("$.status", `is`(updatedBody.status.name))
            }
    }

    @Test
    fun `Should update a Category with optional values successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateCategoryRequestDTO(
            name = randomString(30),
            description = null,
            status = Status.AVAILABLE
        )

        mockMvc.post("/categories") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(body.name))
                jsonPath("$.description", `is`(body.description))
                jsonPath("$.status", `is`(body.status.name))
            }

        val updatedBody = body.run {
            UpdateCategoryRequestDTO(
                name,
                null,
                Status.UNAVAILABLE
            )
        }

        // when
        mockMvc.put("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updatedBody)
        }.andExpect {
            status { isOk() }
        }

        // then
        mockMvc.get("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(updatedBody.name))
                jsonPath("$.description", `is`(updatedBody.description))
                jsonPath("$.status", `is`(updatedBody.status.name))
            }
    }

    @Test
    fun `Should get a Page of Categories successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val requests = List(10) {
            CreateCategoryRequestDTO(
                name = randomString(30),
                description = null,
                status = Status.AVAILABLE
            )
        }

        requests.forEach { body ->
            mockMvc.post("/categories") {
                queryParam("storeId", storeId.toString())
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                }.andExpectAll {
                    jsonPath("$.id", `is`(body.id.toString()))
                    jsonPath("$.name", `is`(body.name))
                    jsonPath("$.description", `is`(body.description))
                    jsonPath("$.status", `is`(body.status.name))
                }
        }

        // when
        mockMvc.get("/categories") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
            }
            .andExpectAll {
                jsonPath("$.limit", `is`(20))
                jsonPath("$.offset", `is`(0))
                jsonPath("$.total", `is`(10))
                requests.sortedBy { body -> body.name }.forEachIndexed { index, body ->
                    jsonPath("$.data[$index].id", `is`(body.id.toString()))
                    jsonPath("$.data[$index].name", `is`(body.name))
                    jsonPath("$.data[$index].description", `is`(body.description))
                    jsonPath("$.data[$index].status", `is`(body.status.name))
                }
            }
    }

    @Test
    fun `Should get a Page of Categories when begins with parameter is empty successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val requests = List(10) {
            CreateCategoryRequestDTO(
                name = randomString(30),
                description = null,
                status = Status.AVAILABLE
            )
        }

        requests.forEach { body ->
            mockMvc.post("/categories") {
                queryParam("storeId", storeId.toString())
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                }.andExpectAll {
                    jsonPath("$.id", `is`(body.id.toString()))
                    jsonPath("$.name", `is`(body.name))
                    jsonPath("$.description", `is`(body.description))
                    jsonPath("$.status", `is`(body.status.name))
                }
        }

        // when
        mockMvc.get("/categories") {
            queryParam("storeId", storeId.toString())
            queryParam("beginsWith", "")
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
            }
            .andExpectAll {
                jsonPath("$.limit", `is`(20))
                jsonPath("$.offset", `is`(0))
                jsonPath("$.total", `is`(10))
                requests.sortedBy { body -> body.name }.forEachIndexed { index, body ->
                    jsonPath("$.data[$index].id", `is`(body.id.toString()))
                    jsonPath("$.data[$index].name", `is`(body.name))
                    jsonPath("$.data[$index].description", `is`(body.description))
                    jsonPath("$.data[$index].status", `is`(body.status.name))
                }
            }
    }

    @Test
    fun `Should get a Page of Categories with name begins with YES successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val nopeRequests = List(10) {
            CreateCategoryRequestDTO(
                name = "NOPE" + randomString(26),
                description = null,
                status = Status.AVAILABLE
            )
        }

        val yesRequests = List(18) {
            CreateCategoryRequestDTO(
                name = "YES" + randomString(27),
                description = null,
                status = Status.AVAILABLE
            )
        }

        (yesRequests + nopeRequests).forEach { body ->
            mockMvc.post("/categories") {
                queryParam("storeId", storeId.toString())
                accept = MediaType.APPLICATION_JSON
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }.andDo { print() }
                .andExpect {
                    status { isOk() }
                }.andExpectAll {
                    jsonPath("$.id", `is`(body.id.toString()))
                    jsonPath("$.name", `is`(body.name))
                    jsonPath("$.description", `is`(body.description))
                    jsonPath("$.status", `is`(body.status.name))
                }
        }

        // when
        mockMvc.get("/categories") {
            queryParam("storeId", storeId.toString())
            queryParam("beginsWith", "YES")
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
            }
            .andExpectAll {
                jsonPath("$.limit", `is`(20))
                jsonPath("$.offset", `is`(0))
                jsonPath("$.total", `is`(yesRequests.size))
                yesRequests.sortedBy { body -> body.name }.forEachIndexed { index, body ->
                    jsonPath("$.data[$index].id", `is`(body.id.toString()))
                    jsonPath("$.data[$index].name", `is`(body.name))
                    jsonPath("$.data[$index].description", `is`(body.description))
                    jsonPath("$.data[$index].status", `is`(body.status.name))
                }
            }
    }

    @Test
    fun `Should delete a Category successfully`() {
        // given
        val storeId = UUID.randomUUID()
        val body = CreateCategoryRequestDTO(
            name = randomString(30),
            description = null,
            status = Status.AVAILABLE
        )

        mockMvc.post("/categories") {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(body)
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpectAll {
                jsonPath("$.id", `is`(body.id.toString()))
                jsonPath("$.name", `is`(body.name))
                jsonPath("$.description", `is`(body.description))
                jsonPath("$.status", `is`(body.status.name))
            }

        // when
        mockMvc.delete("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
            }

        // then
        mockMvc.get("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }
            .andExpect {
                status { isNotFound() }
            }
    }
}
