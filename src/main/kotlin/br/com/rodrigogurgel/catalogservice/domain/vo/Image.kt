package br.com.rodrigogurgel.catalogservice.domain.vo

import java.net.URL

data class Image(
    val path: String,
) {
    init {
        URL(path).toString()
    }
}
