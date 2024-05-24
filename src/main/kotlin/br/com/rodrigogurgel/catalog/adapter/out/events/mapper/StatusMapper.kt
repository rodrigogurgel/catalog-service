package br.com.rodrigogurgel.catalog.adapter.out.events.mapper

import br.com.rodrigogurgel.catalog.domain.Status
import br.com.rodrigogurgel.catalog.out.events.dto.StatusDTO

fun Status.toCategoryCreatedDTO(): StatusDTO {
    return when (this) {
        Status.AVAILABLE -> StatusDTO.AVAILABLE
        Status.UNAVAILABLE -> StatusDTO.UNAVAILABLE
    }
}