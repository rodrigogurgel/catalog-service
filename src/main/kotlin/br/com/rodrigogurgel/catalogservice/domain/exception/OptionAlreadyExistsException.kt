package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Id

class OptionAlreadyExistsException(optionId: Id) :
    IllegalArgumentException("Option $optionId already exists")
