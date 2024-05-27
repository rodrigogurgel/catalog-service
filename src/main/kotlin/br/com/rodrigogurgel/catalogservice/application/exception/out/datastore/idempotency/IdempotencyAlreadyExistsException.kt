package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.idempotency

import java.util.UUID

data class IdempotencyAlreadyExistsException(val idempotencyId: UUID) : RuntimeException(
    "Idempotency $idempotencyId already exists"
)
