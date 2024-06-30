package br.com.rodrigogurgel.catalogservice.framework.config

import br.com.rodrigogurgel.catalogservice.application.port.input.product.CreateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.DeleteProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.GetProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.UpdateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class InputPortConfig {
    @Bean
    fun createProductInputPort(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        productDatastoreOutputPort: ProductDatastoreOutputPort,
    ): CreateProductInputPort {
        return CreateProductInputPort(storeDatastoreOutputPort, productDatastoreOutputPort)
    }

    @Bean
    fun updateProductInputPort(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        productDatastoreOutputPort: ProductDatastoreOutputPort,
    ): UpdateProductInputPort {
        return UpdateProductInputPort(storeDatastoreOutputPort, productDatastoreOutputPort)
    }

    @Bean
    fun getProductUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        productDatastoreOutputPort: ProductDatastoreOutputPort,
    ): GetProductUseCase {
        return GetProductInputPort(storeDatastoreOutputPort, productDatastoreOutputPort)
    }

    @Bean
    fun deleteProductUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        productDatastoreOutputPort: ProductDatastoreOutputPort,
        offerDatastoreOutputPort: OfferDatastoreOutputPort,
    ): DeleteProductUseCase {
        return DeleteProductInputPort(storeDatastoreOutputPort, productDatastoreOutputPort, offerDatastoreOutputPort)
    }
}
