package br.com.rodrigogurgel.catalog.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalog.domain.Status
import br.com.rodrigogurgel.catalog.`in`.events.dto.StatusEventDTO

fun StatusEventDTO.toDomain(): Status {
    return when(this) {
        StatusEventDTO.AVAILABLE -> Status.AVAILABLE
        StatusEventDTO.UNAVAILABLE -> Status.UNAVAILABLE
    }
}