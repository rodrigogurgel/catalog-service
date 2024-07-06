package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Name

class NameLengthException(name: String) :
    IllegalArgumentException(
        "The name value need to be between ${Name.MIN_LENGTH} and ${Name.MAX_LENGTH} characters. " +
            "Actual name value is [$name]."
    )
