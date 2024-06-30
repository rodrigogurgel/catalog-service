package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.framework.adapter.config.DatabaseTestConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.UUID

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [DatabaseTestConfig::class])
@WebAppConfiguration
class ProductControllerTest {
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build()
    }

    @Test
    fun test() {
        mockMvc.get("/products/{id}", UUID.randomUUID()) {
            queryParam("storeId", UUID.randomUUID().toString())
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound() }
            }
    }
}
