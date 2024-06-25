package br.com.rodrigogurgel.catalogservice.domain.vo

data class Description(
    val value: String,
) {
    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 1000
    }

    init {
        require(value.length in MIN_LENGTH..MAX_LENGTH) {
            "The description need be between $MIN_LENGTH and $MAX_LENGTH characters."
        }
    }
}
