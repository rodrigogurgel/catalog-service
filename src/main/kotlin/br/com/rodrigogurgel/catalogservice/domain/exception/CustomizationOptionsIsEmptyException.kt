package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class CustomizationOptionsIsEmptyException(customizationId: Id) :
    IllegalStateException("Customization $customizationId has options empty")
