package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.domain.entity.Product
import br.com.rodrigogurgel.catalogservice.domain.vo.Customization

object CustomizationService {
    fun getAllProducts(customization: Customization): List<Product> {
        return customization.options.flatMap { OptionService.getAllProducts(it) }
    }
}
