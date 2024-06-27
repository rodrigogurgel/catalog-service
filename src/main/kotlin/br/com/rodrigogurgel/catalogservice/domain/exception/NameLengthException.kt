package br.com.rodrigogurgel.catalogservice.domain.exception

class NameLengthException(name: String, minLength: Int, maxLength: Int) :
    IllegalArgumentException(
        """The name value need to be between $minLength and $maxLength characters. 
Actual name value is [$name].
        """.trimIndent()
    )
