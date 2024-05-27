package br.com.rodrigogurgel.catalogservice.application.exception.`in`.rest.option

import br.com.rodrigogurgel.catalogservice.application.exception.`in`.rest.product.ProductNotFoundRestException
import java.util.UUID

data class OptionProductNotFoundRestException(val optionId: UUID, val productId: UUID) :
    ProductNotFoundRestException("Product $productId used by option $optionId not found")
