package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

data class OptionNotFoundException(private val optionId: Id) :
    IllegalStateException("Option with the id $optionId not found")
