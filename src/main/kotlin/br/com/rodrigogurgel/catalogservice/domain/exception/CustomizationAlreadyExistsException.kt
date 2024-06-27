package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CustomizationAlreadyExistsException(customizationId: Id) :
    IllegalArgumentException("Customization $customizationId already exists")
