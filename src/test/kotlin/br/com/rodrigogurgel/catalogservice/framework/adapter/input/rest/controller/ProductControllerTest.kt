package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.framework.adapter.config.DatabaseTestConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.get

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = [DatabaseTestConfig::class])
class ProductControllerTest {
//    private lateinit var mockMvc: MockMvc
//    private val objectMapper = jacksonObjectMapper()
//
//    @Autowired
//    private lateinit var webApplicationContext: WebApplicationContext
//
//    @BeforeEach
//    fun setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build()
//    }
//
//    @Test
//    fun `Should get Product successfully`() {
//        // given
//        val storeId = UUID.randomUUID()
//        val body = CreateProductRequestDTO(
//            name = randomString(30),
//            description = null,
//            image = null
//        )
//
//        // and
//        mockMvc.post(
//            "/products",
//        ) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(body)
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }.andExpectAll {
//                jsonPath("$.id", `is`(body.id.toString()))
//                jsonPath("$.name", `is`(body.name))
//                jsonPath("$.description", `is`(body.description))
//                jsonPath("$.image", `is`(body.image))
//            }
//
//        // when
//        val result = mockMvc.get("/products/{id}", body.id.toString()) {
//            queryParam("storeId", storeId.toString())
//        }
//            .andDo { print() }
//        // then
//        result
//            .andExpect {
//                status { isOk() }
//            }
//            .andExpect {
//                jsonPath("$.id", `is`(body.id.toString()))
//                jsonPath("$.name", `is`(body.name))
//                jsonPath("$.description", `is`(body.description))
//                jsonPath("$.image", `is`(body.image))
//            }
//    }
//
//    @Test
//    fun `Should create a Product successfully`() {
//        // given
//        val storeId = UUID.randomUUID()
//        val body = CreateProductRequestDTO(
//            id = UUID.randomUUID(),
//            name = randomString(30),
//            description = randomString(1000),
//            image = "https://www.image.com.br"
//        )
//
//        // when
//        mockMvc.post(
//            "/products",
//        ) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(body)
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }.andExpectAll {
//                jsonPath("$.id", `is`(body.id.toString()))
//                jsonPath("$.name", `is`(body.name))
//                jsonPath("$.description", `is`(body.description))
//                jsonPath("$.image", `is`(body.image))
//            }
//
//        // then
//        mockMvc.get("/products/{id}", body.id.toString()) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//        }.andExpect {
//            status { isOk() }
//        }.andExpectAll {
//            jsonPath("$.id", `is`(body.id.toString()))
//            jsonPath("$.name", `is`(body.name))
//            jsonPath("$.description", `is`(body.description))
//            jsonPath("$.image", `is`(body.image))
//        }
//    }
//
//    @Test
//    fun `Should update a Product successfully`() {
//        // given
//        val storeId = UUID.randomUUID()
//        val body = CreateProductRequestDTO(
//            id = UUID.randomUUID(),
//            name = randomString(30),
//            description = randomString(1000),
//            image = "https://www.image.com.br"
//        )
//
//        // and
//        mockMvc.post(
//            "/products",
//        ) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(body)
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }.andExpectAll {
//                jsonPath("$.id", `is`(body.id.toString()))
//                jsonPath("$.name", `is`(body.name))
//                jsonPath("$.description", `is`(body.description))
//                jsonPath("$.image", `is`(body.image))
//            }
//
//        // when
//        val updatedBody = body.run {
//            UpdateProductRequestDTO(name, randomString(1000), "https://www.String")
//        }
//        mockMvc.put(
//            "/products/{id}",
//            body.id.toString()
//        ) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(updatedBody)
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }
//
//        // then
//        mockMvc.get("/products/{id}", body.id.toString()) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//        }.andExpectAll {
//            jsonPath("$.id", `is`(body.id.toString()))
//            jsonPath("$.name", `is`(updatedBody.name))
//            jsonPath("$.description", `is`(updatedBody.description))
//            jsonPath("$.image", `is`(updatedBody.image))
//        }
//    }
//
//    @Test
//    fun `Should update a Product optional values successfully`() {
//        // given
//        val storeId = UUID.randomUUID()
//        val body = CreateProductRequestDTO(
//            id = UUID.randomUUID(),
//            name = randomString(30),
//            description = randomString(1000),
//            image = "https://www.image.com.br"
//        )
//
//        // and
//        mockMvc.post(
//            "/products",
//        ) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(body)
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }.andExpectAll {
//                jsonPath("$.id", `is`(body.id.toString()))
//                jsonPath("$.name", `is`(body.name))
//                jsonPath("$.description", `is`(body.description))
//                jsonPath("$.image", `is`(body.image))
//            }
//
//        // when
//        val updatedBody = body.run {
//            UpdateProductRequestDTO(name, null, null)
//        }
//        mockMvc.put(
//            "/products/{id}",
//            body.id.toString()
//        ) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(updatedBody)
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }
//
//        // then
//        mockMvc.get("/products/{id}", body.id.toString()) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//        }.andExpectAll {
//            jsonPath("$.id", `is`(body.id.toString()))
//            jsonPath("$.name", `is`(updatedBody.name))
//            jsonPath("$.description", `is`(updatedBody.description))
//            jsonPath("$.image", `is`(updatedBody.image))
//        }
//    }
//
//    @Test
//    fun `Should delete a Product successfully`() {
//        // given
//        val storeId = UUID.randomUUID()
//        val body = CreateProductRequestDTO(
//            id = UUID.randomUUID(),
//            name = randomString(30),
//            description = randomString(1000),
//            image = "https://www.image.com.br"
//        )
//
//        // and
//        mockMvc.post(
//            "/products",
//        ) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(body)
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }.andExpectAll {
//                jsonPath("$.id", `is`(body.id.toString()))
//                jsonPath("$.name", `is`(body.name))
//                jsonPath("$.description", `is`(body.description))
//                jsonPath("$.image", `is`(body.image))
//            }
//
//        // when
//        mockMvc.delete(
//            "/products/{id}",
//            body.id.toString()
//        ) {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//            content = objectMapper.writeValueAsString(body)
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }
//
//        // then
//        mockMvc.get("/products/{id}", body.id.toString()) {
//            queryParam("storeId", storeId.toString())
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isNotFound() }
//            }
//    }
//
//    @Test
//    fun `Should get a empty Page of Products Successfully`() {
//        val storeId = UUID.randomUUID()
//
//        mockMvc.get("/products") {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }.andExpect {
//                jsonPath("$.total", `is`(0))
//            }
//    }
//
//    @Test
//    fun `Should get a Page of Products successfully`() {
//        // given
//        val requests = List(10) {
//            CreateProductRequestDTO(
//                id = UUID.randomUUID(),
//                name = randomString(30),
//                description = randomString(1000),
//                image = "https://www.image.com.br"
//            )
//        }.sortedBy { body -> body.name }
//        val storeId = UUID.randomUUID()
//
//        // and
//        requests.forEach { body ->
//            mockMvc.post(
//                "/products",
//            ) {
//                queryParam("storeId", storeId.toString())
//                accept = MediaType.APPLICATION_JSON
//                contentType = MediaType.APPLICATION_JSON
//                content = objectMapper.writeValueAsString(body)
//            }
//                .andDo { print() }
//                .andExpect {
//                    status { isOk() }
//                }.andExpectAll {
//                    jsonPath("$.id", `is`(body.id.toString()))
//                    jsonPath("$.name", `is`(body.name))
//                    jsonPath("$.description", `is`(body.description))
//                    jsonPath("$.image", `is`(body.image))
//                }
//        }
//
//        // when
//        mockMvc.get("/products") {
//            queryParam("storeId", storeId.toString())
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }.andExpectAll {
//                requests.forEachIndexed { index, body ->
//                    jsonPath("$.data[$index].id", `is`(body.id.toString()))
//                    jsonPath("$.data[$index].name", `is`(body.name))
//                    jsonPath("$.data[$index].description", `is`(body.description))
//                    jsonPath("$.data[$index].image", `is`(body.image))
//                }
//            }
//    }
//
//    @Test
//    fun `Should get a Page of Products filtered by name successfully`() {
//        // given
//        val nopeProducts = List(10) {
//            CreateProductRequestDTO(
//                id = UUID.randomUUID(),
//                name = "NOPE" + randomString(20),
//                description = randomString(1000),
//                image = "https://www.image.com.br"
//            )
//        }
//        val yesProducts = List(3) {
//            CreateProductRequestDTO(
//                id = UUID.randomUUID(),
//                name = "YES" + randomString(20),
//                description = randomString(1000),
//                image = "https://www.image.com.br"
//            )
//        }
//
//        val requests = (nopeProducts + yesProducts).sortedBy { body -> body.name }
//        val storeId = UUID.randomUUID()
//
//        // and
//        requests.forEach { body ->
//            mockMvc.post(
//                "/products",
//            ) {
//                queryParam("storeId", storeId.toString())
//                accept = MediaType.APPLICATION_JSON
//                contentType = MediaType.APPLICATION_JSON
//                content = objectMapper.writeValueAsString(body)
//            }
//                .andDo { print() }
//                .andExpect {
//                    status { isOk() }
//                }.andExpectAll {
//                    jsonPath("$.id", `is`(body.id.toString()))
//                    jsonPath("$.name", `is`(body.name))
//                    jsonPath("$.description", `is`(body.description))
//                    jsonPath("$.image", `is`(body.image))
//                }
//        }
//
//        // when
//        mockMvc.get("/products") {
//            queryParam("storeId", storeId.toString())
//            queryParam("beginsWith", "YES")
//            accept = MediaType.APPLICATION_JSON
//            contentType = MediaType.APPLICATION_JSON
//        }
//            .andDo { print() }
//            .andExpect {
//                status { isOk() }
//            }.andExpectAll {
//                yesProducts.sortedBy { body -> body.name }.forEachIndexed { index, body ->
//                    jsonPath("$.data[$index].id", `is`(body.id.toString()))
//                    jsonPath("$.data[$index].name", `is`(body.name))
//                    jsonPath("$.data[$index].description", `is`(body.description))
//                    jsonPath("$.data[$index].image", `is`(body.image))
//                }
//            }
//    }
}
