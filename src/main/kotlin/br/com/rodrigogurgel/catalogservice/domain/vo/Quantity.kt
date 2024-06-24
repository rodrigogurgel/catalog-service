package br.com.rodrigogurgel.catalogservice.domain.vo

data class Quantity(
    val minPermitted: Int,
    val maxPermitted: Int,
) {
    init {
        require(minPermitted in 0..maxPermitted && maxPermitted > 0)
    }
}
