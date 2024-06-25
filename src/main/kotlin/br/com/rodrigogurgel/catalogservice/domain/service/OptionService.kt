package br.com.rodrigogurgel.catalogservice.domain.service

import br.com.rodrigogurgel.catalogservice.domain.entity.Option
import br.com.rodrigogurgel.catalogservice.domain.entity.Product

object OptionService {
    fun getAllProducts(option: Option): List<Product> {
        return option.customizations.flatMap { CustomizationService.getAllProducts(it) } + option.product
    }
}
