package br.com.rodrigogurgel.catalogservice.domain.exception

class DescriptionLengthException(description: String, minLength: Int, maxLength: Int) :
    IllegalArgumentException(
        "The description need be between $minLength and $maxLength characters. " +
            "Actual description value is [$description]"
    )
