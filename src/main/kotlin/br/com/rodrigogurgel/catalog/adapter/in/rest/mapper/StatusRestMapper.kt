package br.com.rodrigogurgel.catalog.adapter.`in`.rest.mapper

import br.com.rodrigogurgel.catalog.adapter.`in`.rest.dto.response.StatusResponseDTO
import br.com.rodrigogurgel.catalog.domain.Status

fun Status.toResponseDTO(): StatusResponseDTO {
    return when (this) {
        Status.AVAILABLE -> StatusResponseDTO.AVAILABLE
        Status.UNAVAILABLE -> StatusResponseDTO.UNAVAILABLE
    }
}