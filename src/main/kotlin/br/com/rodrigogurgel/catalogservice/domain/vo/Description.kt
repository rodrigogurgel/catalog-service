package br.com.rodrigogurgel.catalogservice.domain.vo

import br.com.rodrigogurgel.catalogservice.domain.exception.DescriptionLengthException

@JvmInline
value class Description(
    val value: String,
) {
    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 1000
    }

    init {
        if (value.length !in MIN_LENGTH..MAX_LENGTH) throw DescriptionLengthException(value)
    }
}
