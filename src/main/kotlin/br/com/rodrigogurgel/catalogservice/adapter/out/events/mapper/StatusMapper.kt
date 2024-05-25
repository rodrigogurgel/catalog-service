package br.com.rodrigogurgel.catalogservice.adapter.out.events.mapper

import br.com.rodrigogurgel.catalogservice.domain.Status
import br.com.rodrigogurgel.catalogservice.out.events.dto.StatusDTO

fun Status.toCategoryCreatedDTO(): StatusDTO {
    return when (this) {
        Status.AVAILABLE -> StatusDTO.AVAILABLE
        Status.UNAVAILABLE -> StatusDTO.UNAVAILABLE
    }
}