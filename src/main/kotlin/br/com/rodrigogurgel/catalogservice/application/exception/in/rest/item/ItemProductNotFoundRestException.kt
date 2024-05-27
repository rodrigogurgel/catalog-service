package br.com.rodrigogurgel.catalogservice.application.exception.`in`.rest.item

import br.com.rodrigogurgel.catalogservice.application.exception.`in`.rest.product.ProductNotFoundRestException
import java.util.UUID

data class ItemProductNotFoundRestException(val itemId: UUID, val productId: UUID) :
    ProductNotFoundRestException("Product $productId used by item $itemId not found")
