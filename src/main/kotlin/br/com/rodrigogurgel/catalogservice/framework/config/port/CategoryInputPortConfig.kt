package br.com.rodrigogurgel.catalogservice.framework.config.port

import br.com.rodrigogurgel.catalogservice.application.port.input.category.CountCategoriesInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.CreateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.DeleteCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.GetCategoriesInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.GetCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.category.UpdateCategoryInputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CountCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.CreateCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.DeleteCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoriesUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.GetCategoryUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.category.UpdateCategoryUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CategoryInputPortConfig {
    @Bean
    fun getCategoryUseCase(
        storeOutputPort: StoreOutputPort,
        categoryOutputPort: CategoryOutputPort,
    ): GetCategoryUseCase {
        return GetCategoryInputPort(
            storeOutputPort,
            categoryOutputPort
        )
    }

    @Bean
    fun getCategoriesUseCase(
        storeOutputPort: StoreOutputPort,
        categoryOutputPort: CategoryOutputPort,
    ): GetCategoriesUseCase {
        return GetCategoriesInputPort(
            storeOutputPort,
            categoryOutputPort
        )
    }

    @Bean
    fun createCategoryUseCase(
        storeOutputPort: StoreOutputPort,
        categoryOutputPort: CategoryOutputPort,
    ): CreateCategoryUseCase {
        return CreateCategoryInputPort(
            storeOutputPort,
            categoryOutputPort
        )
    }

    @Bean
    fun updateCategoryUseCase(
        storeOutputPort: StoreOutputPort,
        categoryOutputPort: CategoryOutputPort,
    ): UpdateCategoryUseCase {
        return UpdateCategoryInputPort(
            storeOutputPort,
            categoryOutputPort
        )
    }

    @Bean
    fun deleteCategoryUseCase(
        storeOutputPort: StoreOutputPort,
        categoryOutputPort: CategoryOutputPort,
    ): DeleteCategoryUseCase {
        return DeleteCategoryInputPort(
            storeOutputPort,
            categoryOutputPort
        )
    }

    @Bean
    fun countCategoriesUseCase(
        storeOutputPort: StoreOutputPort,
        categoryOutputPort: CategoryOutputPort,
    ): CountCategoriesUseCase {
        return CountCategoriesInputPort(
            storeOutputPort,
            categoryOutputPort
        )
    }
}
