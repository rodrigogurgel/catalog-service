package br.com.rodrigogurgel.catalogservice.application.exception.`in`.rest.product

import java.util.UUID

open class ProductNotFoundRestException protected constructor(override val message: String) :
    RuntimeException(message) {
    constructor(productId: UUID) : this("Product $productId not found")
}
