package br.com.rodrigogurgel.catalogservice.framework.config

import br.com.rodrigogurgel.catalogservice.application.port.input.category.CountCategoriesInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.CreateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.DeleteCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.GetCategoriesInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.GetCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.UpdateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.CountProductsInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.CreateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.DeleteProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.GetProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.GetProductsInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.product.UpdateProductInputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CountCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CreateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.DeleteCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.UpdateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.CountProductsUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.DeleteProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.product.GetProductsUseCase
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

    @Bean
    fun getProductsUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        productDatastoreOutputPort: ProductDatastoreOutputPort,
    ): GetProductsUseCase {
        return GetProductsInputPort(storeDatastoreOutputPort, productDatastoreOutputPort)
    }

    @Bean
    fun countProductsUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        productDatastoreOutputPort: ProductDatastoreOutputPort,
    ): CountProductsUseCase {
        return CountProductsInputPort(storeDatastoreOutputPort, productDatastoreOutputPort)
    }

    @Bean
    fun getCategoryUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    ): GetCategoryUseCase {
        return GetCategoryInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort
        )
    }

    @Bean
    fun getCategoriesUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    ): GetCategoriesUseCase {
        return GetCategoriesInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort
        )
    }

    @Bean
    fun createCategoryUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    ): CreateCategoryUseCase {
        return CreateCategoryInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort
        )
    }

    @Bean
    fun updateCategoryUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    ): UpdateCategoryUseCase {
        return UpdateCategoryInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort
        )
    }

    @Bean
    fun deleteCategoryUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    ): DeleteCategoryUseCase {
        return DeleteCategoryInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort
        )
    }

    @Bean
    fun countCategoriesUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
    ): CountCategoriesUseCase {
        return CountCategoriesInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort
        )
    }
}
