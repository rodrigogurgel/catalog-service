package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

data class CustomizationNotFoundException(private val customizationId: Id) :
    IllegalStateException("Customization with the id $customizationId not found")
