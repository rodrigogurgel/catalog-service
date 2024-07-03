package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.product.CountProductsUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CreateProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductsUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.UpdateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product.CreateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.product.UpdateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.PageResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.ProductResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toEntity
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toResponseDTO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/products")
class ProductController(
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val countProductsUseCase: CountProductsUseCase,
) {
    @GetMapping
    fun getProducts(
        @RequestParam storeId: UUID,
        @RequestParam(defaultValue = "20", required = false) limit: Int,
        @RequestParam(defaultValue = "0", required = false) offset: Int,
        @RequestParam(required = false) beginsWith: String?,
    ): PageResponseDTO<ProductResponseDTO> {
        val total = countProductsUseCase.execute(Id(storeId), beginsWith)
        val products = getProductsUseCase.execute(Id(storeId), limit, offset, beginsWith)

        return PageResponseDTO(
            limit,
            offset,
            beginsWith,
            total,
            products.map { product -> product.toResponseDTO() }
        )
    }

    @PostMapping
    fun createProduct(
        @RequestParam storeId: UUID,
        @RequestBody createProductRequestDTO: CreateProductRequestDTO,
    ): ProductResponseDTO {
        val product = createProductRequestDTO.toEntity()
        createProductUseCase.execute(Id(storeId), product)
        return product.toResponseDTO()
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @RequestParam storeId: UUID,
        @PathVariable(value = "id") productId: UUID,
        @RequestBody updateProductRequestDTO: UpdateProductRequestDTO,
    ) {
        val product = updateProductRequestDTO.toEntity(productId)
        updateProductUseCase.execute(Id(storeId), product)
    }

    @GetMapping("/{id}")
    fun getProductById(@RequestParam storeId: UUID, @PathVariable(value = "id") productId: UUID): ProductResponseDTO {
        return getProductUseCase.execute(Id(storeId), Id(productId)).toResponseDTO()
    }

    @DeleteMapping("/{id}")
    fun deleteProductById(@RequestParam storeId: UUID, @PathVariable(value = "id") productId: UUID) {
        return deleteProductUseCase.execute(Id(storeId), Id(productId))
    }
}
