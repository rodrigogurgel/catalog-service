package br.com.rodrigogurgel.catalogservice.domain.vo

import java.util.UUID

data class Id(
    val id: UUID,
) {
    constructor() : this(UUID.randomUUID())
    constructor(id: String) : this(UUID.fromString(id))
}
