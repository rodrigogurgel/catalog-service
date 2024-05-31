package br.com.rodrigogurgel.catalogservice.adapter.`in`.events.mapper

import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.`in`.events.request.StatusEventRequest

fun StatusEventRequest.toDomain(): Status {
    return when (this) {
        StatusEventRequest.AVAILABLE -> Status.AVAILABLE
        StatusEventRequest.UNAVAILABLE -> Status.UNAVAILABLE
    }
}
