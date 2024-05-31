package br.com.rodrigogurgel.catalogservice.application.exception.`in`.rest.item

import java.util.UUID

data class ItemNotFoundRestException(val itemId: UUID) : RuntimeException(
    "Item with id $itemId not found"
)
