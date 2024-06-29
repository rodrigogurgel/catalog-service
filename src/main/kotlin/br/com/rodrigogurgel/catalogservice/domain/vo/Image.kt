package br.com.rodrigogurgel.catalogservice.domain.vo

import java.net.URL

@JvmInline
value class Image(
    val path: String,
) {
    init {
        URL(path).toString()
    }
}
