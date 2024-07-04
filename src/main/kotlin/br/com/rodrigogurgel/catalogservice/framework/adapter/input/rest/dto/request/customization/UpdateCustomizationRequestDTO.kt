package br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.customization

import br.com.rodrigogurgel.catalogservice.domain.vo.Status
import br.com.rodrigogurgel.catalogservice.framework.adapter.input.rest.dto.request.option.OptionRequestDTO

data class UpdateCustomizationRequestDTO(
    val name: String,
    val description: String?,
    val minPermitted: Int,
    val maxPermitted: Int,
    val status: Status,
    val options: List<OptionRequestDTO>,
)
