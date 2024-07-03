package br.com.rodrigogurgel.catalogservice.framework.config.port

import br.com.rodrigogurgel.catalogservice.application.port.input.offer.CreateOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.DeleteOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.GetOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.UpdateOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CreateOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.DeleteOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OfferInputPortConfig {
    @Bean
    fun createOfferUseCase(
        storeOutputPort: StoreOutputPort,
        categoryOutputPort: CategoryOutputPort,
        productOutputPort: ProductOutputPort,
        offerOutputPort: OfferOutputPort,
    ): CreateOfferUseCase {
        return CreateOfferInputPort(
            storeOutputPort,
            categoryOutputPort,
            productOutputPort,
            offerOutputPort,
        )
    }

    @Bean
    fun getOfferUseCase(
        storeOutputPort: StoreOutputPort,
        offerOutputPort: OfferOutputPort,
    ): GetOfferUseCase {
        return GetOfferInputPort(
            storeOutputPort,
            offerOutputPort
        )
    }

    @Bean
    fun deleteOfferUseCase(
        storeOutputPort: StoreOutputPort,
        offerOutputPort: OfferOutputPort,
    ): DeleteOfferUseCase {
        return DeleteOfferInputPort(
            storeOutputPort,
            offerOutputPort
        )
    }

    @Bean
    fun updateOfferUseCase(
        storeOutputPort: StoreOutputPort,
        offerOutputPort: OfferOutputPort,
        productOutputPort: ProductOutputPort,
    ): UpdateOfferUseCase {
        return UpdateOfferInputPort(
            storeOutputPort,
            productOutputPort,
            offerOutputPort
        )
    }
}
