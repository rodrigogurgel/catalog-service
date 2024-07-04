package br.com.rodrigogurgel.catalogservice.framework.config.port

import br.com.rodrigogurgel.catalogservice.application.port.input.offer.CountOffersInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.CreateOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.DeleteOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.GetOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.GetOffersInputPort
import br.com.rodrigogurgel.catalogservice.application.port.input.offer.UpdateOfferInputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.CategoryDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.OfferDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.ProductDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.port.output.persistence.StoreDatastoreOutputPort
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CountOffersUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.CreateOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.DeleteOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOfferUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.GetOffersUseCase
import br.com.rodrigogurgel.catalogservice.application.usecase.offer.UpdateOfferUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OfferInputPortConfig {
    @Bean
    fun createOfferUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
        productDatastoreOutputPort: ProductDatastoreOutputPort,
        offerDatastoreOutputPort: OfferDatastoreOutputPort,
    ): CreateOfferUseCase {
        return CreateOfferInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort,
            productDatastoreOutputPort,
            offerDatastoreOutputPort,
        )
    }

    @Bean
    fun getOfferUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        offerDatastoreOutputPort: OfferDatastoreOutputPort,
    ): GetOfferUseCase {
        return GetOfferInputPort(
            storeDatastoreOutputPort,
            offerDatastoreOutputPort
        )
    }

    @Bean
    fun deleteOfferUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        offerDatastoreOutputPort: OfferDatastoreOutputPort,
    ): DeleteOfferUseCase {
        return DeleteOfferInputPort(
            storeDatastoreOutputPort,
            offerDatastoreOutputPort
        )
    }

    @Bean
    fun updateOfferUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        offerDatastoreOutputPort: OfferDatastoreOutputPort,
        productDatastoreOutputPort: ProductDatastoreOutputPort,
    ): UpdateOfferUseCase {
        return UpdateOfferInputPort(
            storeDatastoreOutputPort,
            productDatastoreOutputPort,
            offerDatastoreOutputPort
        )
    }

    @Bean
    fun countOffersUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
        offerDatastoreOutputPort: OfferDatastoreOutputPort,
    ): CountOffersUseCase {
        return CountOffersInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort,
            offerDatastoreOutputPort
        )
    }

    @Bean
    fun getOffersUseCase(
        storeDatastoreOutputPort: StoreDatastoreOutputPort,
        categoryDatastoreOutputPort: CategoryDatastoreOutputPort,
        offerDatastoreOutputPort: OfferDatastoreOutputPort,
    ): GetOffersUseCase {
        return GetOffersInputPort(
            storeDatastoreOutputPort,
            categoryDatastoreOutputPort,
            offerDatastoreOutputPort
        )
    }
}
