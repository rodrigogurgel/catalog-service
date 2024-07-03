package br.com.rodrigogurgel.catalogservice.framework.config.port

import br.com.rodrigogurgel.catalogservice.application.port.input.product.CountProductsInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.CreateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.DeleteProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.GetProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.GetProductsInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.UpdateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CountProductsUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductsUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProductInputPortConfig {
    @Bean
    fun createProductInputPort(
        storeOutputPort: StoreOutputPort,
        productOutputPort: ProductOutputPort,
    ): CreateProductInputPort {
        return CreateProductInputPort(storeOutputPort, productOutputPort)
    }

    @Bean
    fun updateProductInputPort(
        storeOutputPort: StoreOutputPort,
        productOutputPort: ProductOutputPort,
    ): UpdateProductInputPort {
        return UpdateProductInputPort(storeOutputPort, productOutputPort)
    }

    @Bean
    fun getProductUseCase(
        storeOutputPort: StoreOutputPort,
        productOutputPort: ProductOutputPort,
    ): GetProductUseCase {
        return GetProductInputPort(storeOutputPort, productOutputPort)
    }

    @Bean
    fun deleteProductUseCase(
        storeOutputPort: StoreOutputPort,
        productOutputPort: ProductOutputPort,
    ): DeleteProductUseCase {
        return DeleteProductInputPort(storeOutputPort, productOutputPort)
    }

    @Bean
    fun getProductsUseCase(
        storeOutputPort: StoreOutputPort,
        productOutputPort: ProductOutputPort,
    ): GetProductsUseCase {
        return GetProductsInputPort(storeOutputPort, productOutputPort)
    }

    @Bean
    fun countProductsUseCase(
        storeOutputPort: StoreOutputPort,
        productOutputPort: ProductOutputPort,
    ): CountProductsUseCase {
        return CountProductsInputPort(storeOutputPort, productOutputPort)
    }
}
