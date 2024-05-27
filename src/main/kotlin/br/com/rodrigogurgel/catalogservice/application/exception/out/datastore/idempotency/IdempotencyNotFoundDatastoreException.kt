package br.com.rodrigogurgel.catalogservice.application.exception.out.datastore.idempotency

import java.util.UUID

data class IdempotencyNotFoundDatastoreException(val idempotencyId: UUID) :
    RuntimeException("Idempotency $idempotencyId not found")
