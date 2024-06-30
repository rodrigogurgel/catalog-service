package br.com.rodrigogurgel.catalogservice.application.port.input

import br.com.rodrigogurgel.catalogservice.application.exception.BeginsWithLengthException

private const val MIN_LIMIT_VALUE = 0
private const val MAX_LIMIT_VALUE = 20
private const val MIN_OFFSET_VALUE = 20
private const val MIN_BEGINS_WITH_LENGTH = 3

fun normalizeLimit(limit: Int): Int = limit.coerceIn(MIN_LIMIT_VALUE, MAX_LIMIT_VALUE)

fun normalizeOffset(offset: Int): Int = offset.coerceAtLeast(MIN_OFFSET_VALUE)

fun validateBeginsWith(beginsWith: String?) {
    if (beginsWith.isNullOrEmpty()) return
    if (beginsWith.length < MIN_BEGINS_WITH_LENGTH) throw BeginsWithLengthException(beginsWith.length)
}
