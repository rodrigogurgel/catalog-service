package br.com.rodrigogurgel.catalogservice.domain

import java.time.OffsetDateTime
import java.util.UUID

data class Idempotency(
    val idempotencyId: UUID?,
    val correlationId: UUID?,
    val storeId: UUID?,
    val status: IdempotencyStatus?,
    val createdAt: OffsetDateTime?,
) {
    enum class IdempotencyStatus {
        CREATED, SUCCESS, FAILURE
    }

    companion object {
        fun created(
            idempotencyId: UUID,
            correlationId: UUID,
            storeId: UUID,
            createdAt: OffsetDateTime? = OffsetDateTime.now(),
        ): Idempotency {
            return Idempotency(idempotencyId, correlationId, storeId, IdempotencyStatus.CREATED, createdAt)
        }

        fun success(
            idempotencyId: UUID,
            correlationId: UUID? = null,
            storeId: UUID? = null,
            createdAt: OffsetDateTime? = null,
        ): Idempotency {
            return Idempotency(idempotencyId, correlationId, storeId, IdempotencyStatus.SUCCESS, createdAt)
        }

        fun failure(
            idempotencyId: UUID,
            correlationId: UUID? = null,
            storeId: UUID? = null,
            createdAt: OffsetDateTime? = null,
        ): Idempotency {
            return Idempotency(idempotencyId, correlationId, storeId, IdempotencyStatus.FAILURE, createdAt)
        }
    }
}
