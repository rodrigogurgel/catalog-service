package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.`in`.events.dto.StatusEventDTO

fun StatusEventDTO.toDomain(): Status {
    return when (this) {
        StatusEventDTO.AVAILABLE -> Status.AVAILABLE
        StatusEventDTO.UNAVAILABLE -> Status.UNAVAILABLE
    }
}
