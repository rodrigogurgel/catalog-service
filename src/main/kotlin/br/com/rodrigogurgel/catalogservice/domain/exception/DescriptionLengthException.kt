package br.com.rodrigogurgel.catalogservice.domain.exception

import br.com.rodrigogurgel.catalogservice.domain.vo.Description

class DescriptionLengthException(description: String) :
    IllegalArgumentException(
        "The description need be between ${Description.MIN_LENGTH} and ${Description.MIN_LENGTH} characters. " +
            "Actual description value is [$description]"
    )
