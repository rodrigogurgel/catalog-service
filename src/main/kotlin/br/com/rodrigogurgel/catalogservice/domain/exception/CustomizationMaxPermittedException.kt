package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CustomizationMaxPermittedException(customizationId: Id) :
    IllegalStateException("Customization $customizationId has max permitted is grater then options size")
