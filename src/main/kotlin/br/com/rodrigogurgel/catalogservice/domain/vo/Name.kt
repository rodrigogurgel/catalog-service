package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.exception.NameLengthException

@JvmInline
value class Name(
    val value: String,
) {
    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 50
    }

    init {
        if (value.length !in MIN_LENGTH..MAX_LENGTH) throw NameLengthException(value, MIN_LENGTH, MAX_LENGTH)
    }
}
