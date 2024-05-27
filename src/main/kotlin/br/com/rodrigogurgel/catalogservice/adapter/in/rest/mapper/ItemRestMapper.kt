package br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.mapper

import br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.dto.response.ItemResponseDTO
import br.com.rodrigogurgel.catalogservice.application.exception.`in`.rest.item.ItemProductNotFoundRestException
import br.com.rodrigogurgel.catalogservice.domain.Customization
import br.com.rodrigogurgel.catalogservice.domain.Item
import br.com.rodrigogurgel.catalogservice.domain.Option
import br.com.rodrigogurgel.catalogservice.domain.Product
import java.util.UUID

fun Item.toResponseDTO(
    products: Map<UUID, Product>,
    customizations: Map<String, List<Customization>>,
    options: Map<String, List<Option>>,
): ItemResponseDTO {
    val product = products[productId] ?: throw ItemProductNotFoundRestException(itemId!!, productId!!)
    return ItemResponseDTO(
        itemId = itemId!!,
        storeId = storeId!!,
        categoryId = categoryId!!,
        product = product.toResponseDTO(),
        price = price!!,
        status = status!!.toResponseDTO(),
        index = index!!,
        customizations = customizations[reference].orEmpty()
            .map { customization -> customization.toResponseDTO(products, customizations, options) },
    )
}
