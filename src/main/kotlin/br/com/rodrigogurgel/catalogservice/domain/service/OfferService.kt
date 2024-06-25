package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.domain.entity.Offer
import br.com.rodrigogurgel.catalogservice.domain.entity.Product

object OfferService {
    fun getAllProducts(offer: Offer): List<Product> {
        return offer.customizations.flatMap { CustomizationService.getAllProducts(it) } + offer.product
    }
}
