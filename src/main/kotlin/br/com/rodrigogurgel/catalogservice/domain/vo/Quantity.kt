package br.com.rodrigogurgel.catalogservice.domain.vo

data class Quantity(
    val minPermitted: Int,
    val maxPermitted: Int,
) {
    init {
        require(maxPermitted > 0) {
            "The maximum permitted must be greater than zero."
        }

        require(minPermitted in 0..maxPermitted) {
            "The minimum permitted need be greater than zero and less or equal than maximum permitted."
        }
    }
}
