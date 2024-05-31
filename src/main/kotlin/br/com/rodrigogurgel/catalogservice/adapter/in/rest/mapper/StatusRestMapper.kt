package br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.mapper

import br.com.rodrigogurgel.catalogservice.adapter.`in`.rest.dto.response.StatusResponseDTO
import br.com.rodrigogurgel.catalogservice.domain.Status

fun Status.toResponseDTO(): StatusResponseDTO {
    return when (this) {
        Status.AVAILABLE -> StatusResponseDTO.AVAILABLE
        Status.UNAVAILABLE -> StatusResponseDTO.UNAVAILABLE
    }
}
