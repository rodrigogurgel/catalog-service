package br.com.rodrigogurgel.catalogservice.adapter.out.datastore.mapper

import br.com.rodrigogurgel.catalogservice.adapter.out.datastore.dto.StatusDatastoreDTO
import br.com.rodrigogurgel.catalogservice.domain.Status

fun Status.toDatastoreDTO(): StatusDatastoreDTO {
    return when (this) {
        Status.AVAILABLE -> StatusDatastoreDTO.AVAILABLE
        Status.UNAVAILABLE -> StatusDatastoreDTO.UNAVAILABLE
    }
}

fun StatusDatastoreDTO.toDomain(): Status {
    return when (this) {
        StatusDatastoreDTO.AVAILABLE -> Status.AVAILABLE
        StatusDatastoreDTO.UNAVAILABLE -> Status.UNAVAILABLE
    }
}
