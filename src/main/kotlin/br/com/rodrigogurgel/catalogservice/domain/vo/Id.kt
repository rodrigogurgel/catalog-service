package br.com.rodrigogurgel.catalogservice.domain.vo

import java.util.UUID

data class Id(
    val value: UUID,
) {
    constructor() : this(UUID.randomUUID())
}
