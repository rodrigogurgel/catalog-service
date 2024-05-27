package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.IdempotencyDatastoreDTO
import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.IdempotencyStatusDatastoreDTO
import br.com.rodrigogurgel.catalogservice.domain.Idempotency
import java.time.OffsetDateTime

fun Idempotency.toDatastoreDTO(): IdempotencyDatastoreDTO {
    return IdempotencyDatastoreDTO(
        idempotencyId = idempotencyId,
        correlationId = correlationId,
        storeId = storeId,
        status = status?.toDatastoreDTO(),
        createdAt = OffsetDateTime.now()
    )
}

fun IdempotencyDatastoreDTO.toDomain(): Idempotency {
    return Idempotency(
        idempotencyId = idempotencyId,
        correlationId = correlationId,
        storeId = storeId,
        status = status?.toDomain(),
        createdAt = OffsetDateTime.now()
    )
}

fun Idempotency.IdempotencyStatus.toDatastoreDTO(): IdempotencyStatusDatastoreDTO {
    return when (this) {
        Idempotency.IdempotencyStatus.CREATED -> IdempotencyStatusDatastoreDTO.CREATED
        Idempotency.IdempotencyStatus.SUCCESS -> IdempotencyStatusDatastoreDTO.SUCCESS
        Idempotency.IdempotencyStatus.FAILURE -> IdempotencyStatusDatastoreDTO.FAILURE
    }
}

fun IdempotencyStatusDatastoreDTO.toDomain(): Idempotency.IdempotencyStatus {
    return when (this) {
        IdempotencyStatusDatastoreDTO.CREATED -> Idempotency.IdempotencyStatus.CREATED
        IdempotencyStatusDatastoreDTO.SUCCESS -> Idempotency.IdempotencyStatus.SUCCESS
        IdempotencyStatusDatastoreDTO.FAILURE -> Idempotency.IdempotencyStatus.FAILURE
    }
}
