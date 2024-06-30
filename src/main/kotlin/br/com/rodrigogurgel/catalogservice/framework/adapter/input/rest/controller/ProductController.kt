package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.controller

import br.com.rodrigogurgel.catalogservice.application.usecase.product.CreateProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.UpdateProductUseCase
import br.com.rodrigogurgel.catalogservice.domain.vo.Id
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.CreateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.UpdateProductRequestDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.response.ProductResponseDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toDTO
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.mapper.toDomain
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
) {
    @PostMapping
    fun create(
        @RequestParam storeId: UUID,
        @RequestBody createProductRequestDTO: CreateProductRequestDTO,
    ): ProductResponseDTO {
        val product = createProductRequestDTO.toDomain()
        createProductUseCase.execute(Id(storeId), product)
        return product.toDTO()
    }

    @PutMapping("/{id}")
    fun update(
        @RequestParam storeId: UUID,
        @PathVariable id: UUID,
        @RequestBody updateProductRequestDTO: UpdateProductRequestDTO,
    ) {
        val product = updateProductRequestDTO.toDomain(id)
        updateProductUseCase.execute(Id(storeId), product)
    }

    @GetMapping("/{id}")
    fun getProductById(@RequestParam storeId: UUID, @PathVariable id: UUID): ProductResponseDTO {
        return getProductUseCase.execute(Id(storeId), Id(id)).toDTO()
    }

    @DeleteMapping("/{id}")
    fun deleteProductById(@RequestParam storeId: UUID, @PathVariable id: UUID) {
        return deleteProductUseCase.execute(Id(storeId), Id(id))
    }
}
