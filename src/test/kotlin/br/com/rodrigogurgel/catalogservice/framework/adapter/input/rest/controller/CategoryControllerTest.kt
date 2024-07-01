package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.fixture.randomString
import br.com.rodrigogurgel.catalogservice.framework.adapter.config.DatabaseTestConfig
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.category.CreateCategoryRequestDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
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
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.status").value(body.status)
            }

        // then
        mockMvc.get("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.status").value(body.status)
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
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.status").value(body.status)
            }

        // then
        mockMvc.get("/categories/{id}", body.id) {
            queryParam("storeId", storeId.toString())
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andDo { print() }
            .andExpect {
                status { isOk() }
            }.andExpect {
                MockMvcResultMatchers.jsonPath("$.id").value(body.id)
                MockMvcResultMatchers.jsonPath("$.name").value(body.name)
                MockMvcResultMatchers.jsonPath("$.description").value(body.description)
                MockMvcResultMatchers.jsonPath("$.status").value(body.status)
            }
    }
}
