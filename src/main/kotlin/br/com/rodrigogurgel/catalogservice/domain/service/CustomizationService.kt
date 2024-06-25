package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.domain.entity.Customization
import br.com.rodrigogurgel.catalogservice.domain.entity.Product

object CustomizationService {
    fun getAllProducts(customization: Customization): List<Product> {
        return customization.options.flatMap { OptionService.getAllProducts(it) }
    }
}
