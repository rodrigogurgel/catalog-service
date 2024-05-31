package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper.response

import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.out.events.response.StatusEventResponse

fun Status.toStatusEventResponse(): StatusEventResponse {
    return when (this) {
        Status.AVAILABLE -> StatusEventResponse.AVAILABLE
        Status.UNAVAILABLE -> StatusEventResponse.UNAVAILABLE
    }
}
